package com.yanyan.core.sml;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Title: XML帮助工具
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class XmlHelper {
    public static String unwrap(String xml, String wrapper) {
        if (xml != null) {
            String head = "";
            String body = "";
            xml = xml.trim();
            if (xml.startsWith("<?xml")) {
                head = StringUtils.substring(xml, 0, StringUtils.indexOf(xml, "?>") + 2);
                body = StringUtils.substringAfter(xml, "?>");
                body = body.trim();
            } else {
                body = xml;
            }

            if (body.startsWith("<" + wrapper + "/>")) {
                return "";
            } else if (body.startsWith("<" + wrapper + ">")) {
                return head + StringUtils.substringBetween(body, "<" + wrapper + ">", "</" + wrapper + ">").trim();
            }
        }
        return xml;
    }

    public static String wrap(String xml, String wrapper) {
        if (xml != null) {
            String head = "";
            String body = "";
            xml = xml.trim();
            if (xml.startsWith("<?xml")) {
                head = StringUtils.substring(xml, 0, StringUtils.indexOf(xml, "?>") + 2);
                body = StringUtils.substringAfter(xml, "?>");
                body = body.trim();
            } else {
                body = xml;
            }
            return head + "<" + wrapper + ">" + body + "</" + wrapper + ">";
        }

        return "<" + wrapper + "></" + wrapper + ">";
    }

    /**
     * Filter the specified message string for characters that are sensitive in XML. This avoids conflict to XML.
     *
     * @param xml The message string to be filtered
     */
    public static String escape(String xml) {
        XMLFilter ft = new XMLFilter();
        ft.string(xml);

        return ft.buf.toString();
    }

    public static String unescape(String xml) {
        return StringEscapeUtils.unescapeXml(xml);
    }

    private static final class XMLFilter {
        StringBuffer buf = new StringBuffer();

        private void character(char ch) {
            switch (ch) {
      /*case '\''://内容中单引号和双引号不是特殊字符，属性中才是
        buf.append("&apos;");
        break;
      case '\"':
        buf.append("&quot;");
        break;*/
                case '<':
                    buf.append("&lt;");
                    break;
                case '>':
                    buf.append("&gt;");
                    break;
                case '&':
                    buf.append("&amp;");
                    break;
                default:
                    if ((ch >= 0x00 && ch <= 0x08) || (ch >= 0x0b && ch <= 0x0c) || (ch >= 0x0e && ch <= 0x1f)) { // !(\\x00-\\x08 \\x0b-\\x0c
                        // \\x0e-\\x1f)*/0-31除了回车换行
                    } else {
                        buf.append(ch);
                    }
            }
        }

        private void string(String str) {
            if (str == null)
                return;
            int length = str.length();
            for (int i = 0; i < length; i++) {
                character(str.charAt(i));
            }
        }
    }
}
