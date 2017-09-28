package org.rdf4led.rdf.dictionary.vocab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LangTagGen {
  private static String[] listLang =
      new String[] {
        "AF", "AL", "DZ", "AS", "AD", "AO", "AI", "AQ", "AG", "AR", "AM", "AW", "AU", "AT", "AZ",
        "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ", "BM", "BT", "BO", "BA", "BW", "BV", "BR",
        "IO", "BN", "BG", "BF", "BI", "KH", "CM", "CA", "CV", "KY", "CF", "TD", "CL", "CN", "CX",
        "CC", "CO", "KM", "CG", "CD", "CK", "CR", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ", "DM",
        "DO", "TP", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF",
        "PF", "TF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU", "GT", "GN",
        "GW", "GY", "HT", "HM", "VA", "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IL",
        "IT", "JM", "JP", "JO", "KZ", "KE", "KI", "KP", "KR", "KW", "KG", "LA", "LV", "LB", "LS",
        "LR", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MQ",
        "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "MS", "MA", "MZ", "MM", "NA", "NR", "NP",
        "NL", "AN", "NC", "NZ", "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS",
        "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", "RE", "RO", "RU", "RW", "SH",
        "KN", "LC", "PM", "VC", "WS", "SM", "ST", "SA", "SN", "SC", "SL", "SG", "SK", "SI", "SB",
        "SO", "ZA", "GS", "ES", "LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TJ", "TZ",
        "TH", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA", "AE", "GB", "US",
        "UM", "UY", "UZ", "VU", "VE", "VN", "VG", "VI", "WF", "EH", "YE", "YU", "ZM", "ZW", "EN",
        "JA", "ZH", "KO"
      };

  public static void main(String[] args) {
    String fileOut =
        "/home/anhlt185/projects/RDF_embedded_iot/e_rdf/src/main/java/dev/insight/rdf4led/org.rdf4led.rdf.dictionary/vocab/LangTag.java";

    File file = new File(fileOut);

    System.out.println(file.getAbsolutePath());

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));

      writer.write("package org.rdf4led.org.rdf4led.rdf.dictionary.vocab; \n\n\n");

      writer.write("public class LangTag\n");

      writer.write("{\n");

      for (int i = 0; i < listLang.length; i++) {
        writer.write(
            "\tprivate static final byte " + listLang[i] + " = " + (128 - (i + 1)) + "; \n");
      }

      writer.write("\n\n");

      writer.write("\tpublic static LangTag langtag = new LangTag(); \n\n");

      writer.write("\tprivate LangTag() {} \n\n");

      writer.write("\tpublic byte getTag(String org.rdf4led.rdf.org.rdf4led.sparql.parser.lang) \n");

      writer.write("\t{\n");

      writer.write("\t\tswitch (org.rdf4led.rdf.org.rdf4led.sparql.parser.lang) {\n");

      for (int i = 0; i < listLang.length; i++) {
        writer.write(
            "\t\t\tcase \"" + listLang[i].toLowerCase() + "\" : return " + listLang[i] + "; \n");
      }

      writer.write("\t\t\tdefault: \n");

      writer.write(
          "\t\t\t\tthrow new IllegalArgumentException(org.rdf4led.rdf.org.rdf4led.sparql.parser.lang + \" unknow org.rdf4led.rdf.org.rdf4led.sparql.parser.lang\"); \n \t\t}\n");

      writer.write("\t}\n");

      writer.write("\n\n");

      writer.write("\tpublic String toLang(byte langTag) \n");

      writer.write("\t{\n");

      writer.write("\t\tswitch (langTag) {\n");

      for (int i = 0; i < listLang.length; i++) {
        writer.write(
            "\t\t\tcase "
                + listLang[i]
                + " : return "
                + "\""
                + listLang[i].toLowerCase()
                + "\""
                + "; \n");
      }

      writer.write("\t\t\tdefault: \n");

      writer.write(
          "\t\t\t\tthrow new IllegalArgumentException(langTag + \" unknown org.rdf4led.rdf.org.rdf4led.sparql.parser.lang tag\"); \n \t\t}\n");

      writer.write("\t}\n");

      writer.write("}\n");

      writer.flush();

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
