package com.cooler.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Properties utils, it's easy to use like Propers.load(...).get..(...).
 * This is used to be instead of shared preference cause it's muti-process problem.
 *
 * @author Vincent Cheung
 * @since  Feb. 05, 2014
 */
public class Propers {
	private static final String TAG = Propers.class.getSimpleName();
	private static final String PROPERTY_NAME = "properties";
	private static final String XML_SUFFIX = ".xml";

	private static Propers sInstance;
	private Context mContext;

	/** keep single instance */
	private Propers(Context context) {
		mContext = context;
	}

	/**
	 * Get global instance of Propers.
	 *
	 * @param  context context
	 * @return         the single instanfe of Propers
	 */
	public static Propers with(Context context) {
		if (sInstance == null) {
			synchronized (Propers.class) {
				sInstance = new Propers(context);
			}
		}

		return sInstance;
	}

	/** custom xml properties */
	private class XMLProperties extends Properties {
		private static final String PROP_DTD_NAME = "http://java.sun.com/dtd/properties.dtd";
		private static final String PROP_DTD = "<?xml version=\'1.0\' encoding=\'utf-8\'?>"
				+ "    <!ELEMENT properties (comment?, entry*) >"
				+ "    <!ATTLIST properties version CDATA #FIXED \"1.0\" >"
				+ "    <!ELEMENT comment (#PCDATA) >"
				+ "    <!ELEMENT entry (#PCDATA) >"
				+ "    <!ATTLIST entry key CDATA #REQUIRED >";

		private static final int TYPE_INT = 1;
		private static final int TYPE_LONG = 2;
		private static final int TYPE_BOOLEAN = 3;
		private static final int TYPE_FLOAT = 4;
		private static final int TYPE_DOUBLE = 5;
		private static final int TYPE_STRING = 6;

		private transient DocumentBuilder mBuilder = null;

		@Override
		public synchronized void loadFromXML(InputStream in) throws IOException {
			if (in == null) {
				throw new NullPointerException("in == null");
			}

			if (mBuilder == null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				try {
					mBuilder = factory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					throw new Error(e);
				}

				mBuilder.setErrorHandler(new ErrorHandler() {
					public void warning(SAXParseException e) throws SAXException {
						throw e;
					}

					public void error(SAXParseException e) throws SAXException {
						throw e;
					}

					public void fatalError(SAXParseException e) throws SAXException {
						throw e;
					}
				});

				mBuilder.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(String publicId, String systemId) throws
							SAXException, IOException {
						if (systemId.equals(PROP_DTD_NAME)) {
							InputSource result = new InputSource(new StringReader(PROP_DTD));
							result.setSystemId(PROP_DTD_NAME);
							return result;
						}
						throw new SAXException("Invalid DOCTYPE declaration: " + systemId);
					}
				});
			}

			try {
				Document doc = mBuilder.parse(in);
				NodeList intEntries = doc.getElementsByTagName("int");
				NodeList longEntries = doc.getElementsByTagName("long");
				NodeList booleanEntries = doc.getElementsByTagName("boolean");
				NodeList floatEntries = doc.getElementsByTagName("float");
				NodeList doubleEntries = doc.getElementsByTagName("double");
				NodeList stringEntries = doc.getElementsByTagName("string");

				getKeyValue(intEntries, TYPE_INT);
				getKeyValue(longEntries, TYPE_LONG);
				getKeyValue(booleanEntries, TYPE_BOOLEAN);
				getKeyValue(floatEntries, TYPE_FLOAT);
				getKeyValue(doubleEntries, TYPE_DOUBLE);
				getKeyValue(stringEntries, TYPE_STRING);
			} catch (IOException e) {
				throw e;
			} catch (SAXException e) {
				throw new InvalidPropertiesFormatException(e);
			}
		}

		@Override
		public void storeToXML(OutputStream os, String comment) throws IOException {
			storeToXML(os, comment, "UTF-8");
		}

		@Override
		public synchronized void storeToXML(OutputStream os, String comment,
				String encoding) throws IOException {
			if (os == null) {
				throw new NullPointerException("os == null");
			} else if (encoding == null) {
				throw new NullPointerException("encoding == null");
			}

	        /*
	         * We can write to XML file using encoding parameter but note that some
	         * aliases for encodings are not supported by the XML parser. Thus we
	         * have to know canonical name for encoding used to store data in XML
	         * since the XML parser must recognize encoding name used to store data.
	         */

			String encodingCanonicalName;
			try {
				encodingCanonicalName = Charset.forName(encoding).name();
			} catch (IllegalCharsetNameException e) {
				System.out.println("Warning: encoding name " + encoding
						+ " is illegal, using UTF-8 as default encoding");
				encodingCanonicalName = "utf-8";
			} catch (UnsupportedCharsetException e) {
				System.out.println("Warning: encoding " + encoding
						+ " is not supported, using UTF-8 as default encoding");
				encodingCanonicalName = "utf-8";
			}

			PrintStream printStream = new PrintStream(os, false,
					encodingCanonicalName);

			printStream.print("<?xml version=\'1.0\' encoding=\'");
			printStream.print(encodingCanonicalName.toLowerCase(Locale.US));
			printStream.println("\'?>");

			printStream.print("<!DOCTYPE properties SYSTEM \'");
			printStream.print(PROP_DTD_NAME);
			printStream.println("\'>");

			printStream.println("<map>");

			if (comment != null) {
				printStream.print("<comment>");
				printStream.print(substitutePredefinedEntries(comment));
				printStream.println("</comment>");
			}

			for (Entry<Object, Object> entry : entrySet()) {
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				if (value instanceof Integer) {
					printStream.print("<int name=\"");
				} else if (value instanceof Long) {
					printStream.print("<long name=\"");
				} else if (value instanceof Boolean) {
					printStream.print("<boolean name=\"");
				} else if (value instanceof Float) {
					printStream.print("<float name=\"");
				} else if (value instanceof Double) {
					printStream.print("<double name=\"");
				} else {
					printStream.print("<string name=\"");
				}

				printStream.print(substitutePredefinedEntries(key));
				printStream.print("\" value=\"");
				if (value instanceof Integer) {
					printStream.print(Integer.toString((int) value));
				} else if (value instanceof Long) {
					printStream.print(Long.toString((long) value));
				} else if (value instanceof Boolean) {
					printStream.print(Boolean.toString((boolean) value));
				} else if (value instanceof Float) {
					printStream.print(Float.toString((float) value));
				} else if (value instanceof Double) {
					printStream.print(Double.toString((double) value));
				} else {
					printStream.print(substitutePredefinedEntries((String) value));
				}

				printStream.print("\" />\n");
			}

			printStream.println("</map>");
			printStream.flush();
		}

		private void getKeyValue(NodeList entries, int type) {
			if (entries == null) {
				return;
			}

			int entriesListLength = entries.getLength();
			for (int i = 0; i < entriesListLength; i++) {
				Element entry = (Element) entries.item(i);
				String key = entry.getAttribute("name");
				String value = entry.getAttribute("value");

				/* key != null & value != null but key or(and) value can be empty String */
				switch (type) {
				case TYPE_INT:
					put(key, Integer.parseInt(value));
					break;

				case TYPE_LONG:
					put(key, Long.parseLong(value));
					break;

				case TYPE_BOOLEAN:
					put(key, Boolean.parseBoolean(value));
					break;

				case TYPE_FLOAT:
					put(key, Float.parseFloat(value));
					break;

				case TYPE_DOUBLE:
					put(key, Double.parseDouble(value));
					break;

				case TYPE_STRING:
				default:
					put(key, value);
					break;
				}
			}
		}

		private String substitutePredefinedEntries(String s) {
			/* substitution for predefined character entities to use them safely in XML */
			s = s.replaceAll("&", "&amp;");
			s = s.replaceAll("<", "&lt;");
			s = s.replaceAll(">", "&gt;");
			s = s.replaceAll("'", "&apos;");
			s = s.replaceAll("\"", "&quot;");
			return s;
		}
	}

	/**
	 * Property class, used to save or read properties from file.
	 */
	public class Property {
		private String mProperFilePath;

		protected Property(String properFilePath) {
			mProperFilePath = properFilePath;
		}

		/** save properties into file */
		private void save(XMLProperties properties, String filePath) {
			try {
				FileOutputStream fos = new FileOutputStream(filePath);
				properties.storeToXML(fos, null);
				fos.close();
			} catch (IOException e) {
				Log.e(TAG, "save property error: " + e.getMessage());
			}
		}

		/** load properties from file */
		private XMLProperties load(String filePath) {
			XMLProperties properties = new XMLProperties();
			try {
				FileInputStream fis = new FileInputStream(filePath);
				properties.loadFromXML(fis);
				fis.close();
			} catch (IOException e) {
				/* ignore */
			}

			return properties;
		}

		/**
		 * Save string value into properties.
		 *
		 * @param key   key in properties
		 * @param value string value
		 */
		public void save(String key, Object value) {
			XMLProperties properties = load(mProperFilePath);
			properties.put(key, value);
			save(properties, mProperFilePath);
		}

		/**
		 * Save all value into properties.
		 *
		 * @param map key/value map to save
		 */
		public void save(Map<String, Object> map) {
			XMLProperties properties = new XMLProperties();
			properties.putAll(map);
			save(properties, mProperFilePath);
		}

		/**
		 * Get integer from properties with specified key.
		 *
		 * @param key          key in properties
		 * @param defaultValue default value
		 * @return             value if existed, otherwise reutrn default value
		 */
		public int getInt(String key, int defaultValue) {
			try {
				return (int) load(mProperFilePath).get(key);
			} catch (NullPointerException | ClassCastException e) {
				return defaultValue;
			}
		}

		/**
		 * Get long from properties with specified key.
		 *
		 * @param key          key in properties
		 * @param defaultValue default value
		 * @return             value if existed, otherwise reutrn default value
		 */
		public long getLong(String key, long defaultValue) {
			try {
				return (long) load(mProperFilePath).get(key);
			} catch (NullPointerException | ClassCastException e) {
				return defaultValue;
			}
		}

		/**
		 * Get float from properties with specified key.
		 *
		 * @param key          key in properties
		 * @param defaultValue default value
		 * @return             value if existed, otherwise reutrn default value
		 */
		public float getFloat(String key, float defaultValue) {
			try {
				return (float) load(mProperFilePath).get(key);
			} catch (NullPointerException | ClassCastException e) {
				return defaultValue;
			}
		}

		/**
		 * Get boolean from properties with specified key.
		 *
		 * @param key          key in properties
		 * @param defaultValue default value
		 * @return             value if existed, otherwise reutrn default value
		 */
		public boolean getBoolean(String key, boolean defaultValue) {
			try {
				return (boolean) load(mProperFilePath).get(key);
			} catch (NullPointerException | ClassCastException e) {
				return defaultValue;
			}
		}

		/**
		 * Get string from properties with specified key.
		 *
		 * @param key          key in properties
		 * @param defaultValue default value
		 * @return             value if existed, otherwise reutrn default value
		 */
		public String getString(String key, String defaultValue) {
			String value = (String) load(mProperFilePath).get(key);
			return value == null ? defaultValue : value;
		}

		/**
		 * To check if the specified key existed.
		 *
		 * @param  key key in properties
		 * @return     true if existed, otherwise return false
		 */
		public boolean contains(String key) {
			return load(mProperFilePath).containsKey(key);
		}

		/**
		 * Removes the key/value pair with the specified key.
		 *
		 * @param key key in properties
		 */
		public void remove(String key) {
			load(mProperFilePath).remove(key);
		}

		/**
		 * Clear all the key/value in properties
		 */
		public void clear() {
			load(mProperFilePath).clear();
		}
	}

	/** get property file path */
	private String getPropertyFilePath(Context context, String properFileName) {
		properFileName = (TextUtils.isEmpty(properFileName) ?
			 context.getPackageName() + "_" + PROPERTY_NAME : properFileName) + XML_SUFFIX;

		String dirPath = context.getFilesDir() + File.separator + PROPERTY_NAME;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		return context.getFilesDir() + File.separator + PROPERTY_NAME +
				File.separator + properFileName;
	}

	/**
	 * Load properties from file according to directory and file name.
	 *
	 * @param properDir      absolute directory
	 * @param properFileName file name
	 * @return               {@link Property}
	 */
	public Property load(String properDir, String properFileName) {
		String dirPath = properDir + File.separator + PROPERTY_NAME;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		return new Property(properDir + File.separator + properFileName);
	}

	/**
	 * Load properties from file system.
	 *
	 * @param  properFileName the name of property file
	 * @return                {@link Property}
	 */
	public Property load(String properFileName) {
		return new Property(getPropertyFilePath(mContext, properFileName));
	}

	/**
	 * Load properties from file system with default name.
	 *
	 * @return {@link Property}
	 */
	public Property load() {
		return new Property(getPropertyFilePath(mContext, null));
	}
}
