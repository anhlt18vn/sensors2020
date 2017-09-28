package org.rdf4led.rdf.dictionary.vocab;

/**
 * FOAF.java Author : Anh Le-Tuan
 *
 * <p>Contact: anh.letuan@insight-centre.org anh.le@deri.org
 *
 * <p>Nov 27, 2015
 */
public class FOAF implements Vocabulary {

  public static Vocabulary foaf = new FOAF();

  private FOAF() {}

  static final String prefix = "http://xmlns.com/foaf/0.1/";

  static final byte c_prefix = VocabHandler.FOAF_;

  public static final byte Agent = 0;
  public static final byte Document = 1;
  public static final byte Group = 2;
  public static final byte Image = 3;
  public static final byte LabelProperty = 4;
  public static final byte OnlineAccount = 5;
  public static final byte OnlineChatAccount = 6;
  public static final byte OnlineEcommerceAccount = 7;
  public static final byte OnlineGamingAccount = 8;
  public static final byte Organization = 9;
  public static final byte Person = 10;
  public static final byte PersonalProfileDocument = 11;
  public static final byte Project = 12;
  public static final byte account = 13;
  public static final byte accountName = 14;
  public static final byte accountServiceHomepage = 15;
  public static final byte age = 16;
  public static final byte aimChatID = 17;
  public static final byte based_near = 18;
  public static final byte birthday = 19;
  public static final byte currentProject = 20;
  public static final byte depiction = 21;
  public static final byte depicts = 22;
  public static final byte dnaChecksum = 23;
  public static final byte familyName = 24;
  public static final byte family_name = 25;
  public static final byte firstName = 26;
  public static final byte focus = 27;
  public static final byte fundedBy = 28;
  public static final byte geekcode = 29;
  public static final byte gender = 30;
  public static final byte givenName = 31;
  public static final byte givenname = 32;
  public static final byte holdsAccount = 33;
  public static final byte homepage = 34;
  public static final byte icqChatID = 35;
  public static final byte img = 36;
  public static final byte interest = 37;
  public static final byte isPrimaryTopicOf = 38;
  public static final byte jabberID = 39;
  public static final byte knows = 40;
  public static final byte lastName = 41;
  public static final byte logo = 42;
  public static final byte made = 43;
  public static final byte maker = 44;
  public static final byte mbox = 45;
  public static final byte mbox_sha1sum = 46;
  public static final byte member = 47;
  public static final byte membershipClass = 48;
  public static final byte msnChatID = 49;
  public static final byte myersBriggs = 50;
  public static final byte name = 51;
  public static final byte nick = 52;
  public static final byte openid = 53;
  public static final byte page = 54;
  public static final byte pastProject = 55;
  public static final byte phone = 56;
  public static final byte plan = 57;
  public static final byte primaryTopic = 58;
  public static final byte publications = 59;
  public static final byte schoolHomepage = 60;
  public static final byte sha1 = 61;
  public static final byte skypeID = 62;
  public static final byte status = 63;
  public static final byte surname = 64;
  public static final byte theme = 65;
  public static final byte thumbnail = 66;
  public static final byte tipjar = 67;
  public static final byte title = 68;
  public static final byte topic = 69;
  public static final byte topic_interest = 70;
  public static final byte weblog = 71;
  public static final byte workInfoHomepage = 72;
  public static final byte workplaceHomepage = 73;
  public static final byte yahooChatID = 74;

  @Override
  public byte getPrefix() {
    return c_prefix;
  }

  @Override
  public String toLexical(byte suffix) {
    // node -> byte
    switch (suffix) {
      case Agent:
        return prefix + "Agent";
      case Document:
        return prefix + "Document";
      case Group:
        return prefix + "Group";
      case Image:
        return prefix + "Image";
      case LabelProperty:
        return prefix + "LabelProperty";
      case OnlineAccount:
        return prefix + "OnlineAccount";
      case OnlineChatAccount:
        return prefix + "OnlineChatAccount";
      case OnlineEcommerceAccount:
        return prefix + "OnlineEcommerceAccount";
      case OnlineGamingAccount:
        return prefix + "OnlineGamingAccount";
      case Organization:
        return prefix + "Organization";
      case PersonalProfileDocument:
        return prefix + "PersonalProfileDocument";
      case Person:
        return prefix + "Person";
      case Project:
        return prefix + "Project";
      case account:
        return prefix + "account";
      case accountName:
        return prefix + "accountName";
      case accountServiceHomepage:
        return prefix + "accountServiceHomepage";
      case age:
        return prefix + "age";
      case aimChatID:
        return prefix + "aimChatID";
      case based_near:
        return prefix + "based_near";
      case birthday:
        return prefix + "birthday";
      case currentProject:
        return prefix + "currentProject";
      case depiction:
        return prefix + "depiction";
      case depicts:
        return prefix + "depicts";
      case dnaChecksum:
        return prefix + "dnaChecksum";
      case familyName:
        return prefix + "familyName";
      case family_name:
        return prefix + "family_name";
      case firstName:
        return prefix + "firstName";
      case focus:
        return prefix + "focus";
      case fundedBy:
        return prefix + "fundedBy";
      case geekcode:
        return prefix + "geekcode";
      case gender:
        return prefix + "gender";
      case givenName:
        return prefix + "givenName";
      case givenname:
        return prefix + "givenname";
      case holdsAccount:
        return prefix + "holdsAccount";
      case homepage:
        return prefix + "homepage";
      case icqChatID:
        return prefix + "icqChatID";
      case img:
        return prefix + "img";
      case interest:
        return prefix + "interest";
      case isPrimaryTopicOf:
        return prefix + "isPrimaryTopicOf";
      case jabberID:
        return prefix + "jabberID";
      case knows:
        return prefix + "knows";
      case lastName:
        return prefix + "lastName";
      case logo:
        return prefix + "logo";
      case made:
        return prefix + "made";
      case maker:
        return prefix + "maker";
      case mbox:
        return prefix + "mbox";
      case mbox_sha1sum:
        return prefix + "mbox_sha1sum";
      case member:
        return prefix + "member";
      case membershipClass:
        return prefix + "membershipClass";
      case msnChatID:
        return prefix + "msnChatID";
      case myersBriggs:
        return prefix + "myersBriggs";
      case name:
        return prefix + "name";
      case nick:
        return prefix + "nick";
      case openid:
        return prefix + "openid";
      case page:
        return prefix + "page";
      case pastProject:
        return prefix + "pastProject";
      case phone:
        return prefix + "phone";
      case plan:
        return prefix + "plan";
      case primaryTopic:
        return prefix + "primaryTopic";
      case publications:
        return prefix + "publications";
      case schoolHomepage:
        return prefix + "schoolHomepage";
      case sha1:
        return prefix + "sha1";
      case skypeID:
        return prefix + "skypeID";
      case status:
        return prefix + "status";
      case surname:
        return prefix + "surname";
      case theme:
        return prefix + "theme";
      case thumbnail:
        return prefix + "thumbnail";
      case tipjar:
        return prefix + "tipjar";
      case title:
        return prefix + "title";
      case topic:
        return prefix + "topic";
      case topic_interest:
        return prefix + "topic_interest";
      case weblog:
        return prefix + "weblog";
      case workInfoHomepage:
        return prefix + "workInfoHomepage";
      case workplaceHomepage:
        return prefix + "workplaceHomepage";
      case yahooChatID:
        return prefix + "yahooChatID";
      case error:
        return prefix + "error";
      default:
        throw new IllegalArgumentException("Can not recognise suffix: " + suffix);
    }
  }

  @Override
  public byte getSuffix(String lexical) {
    if (lexical.contains("LabelProperty")) return (LabelProperty);
    if (lexical.contains("OnlineChatAccount")) return (OnlineChatAccount);
    if (lexical.contains("OnlineAccount")) return (OnlineAccount);
    if (lexical.contains("OnlineEcommerceAccount")) return (OnlineEcommerceAccount);
    if (lexical.contains("OnlineGamingAccount")) return (OnlineGamingAccount);
    if (lexical.contains("PersonalProfileDocument")) return (PersonalProfileDocument);
    if (lexical.contains("accountName")) return (accountName);
    if (lexical.contains("accountServiceHomepage")) return (accountServiceHomepage);
    if (lexical.contains("aimChatID")) return (aimChatID);
    if (lexical.contains("based_near")) return (based_near);
    if (lexical.contains("birthday")) return (birthday);
    if (lexical.contains("currentProject")) return (currentProject);
    if (lexical.contains("dnaChecksum")) return (dnaChecksum);
    if (lexical.contains("familyName")) return (familyName);
    if (lexical.contains("family_name")) return (family_name);
    if (lexical.contains("firstName")) return (firstName);
    if (lexical.contains("fundedBy")) return (fundedBy);
    if (lexical.contains("geekcode")) return (geekcode);
    if (lexical.contains("gender")) return (gender);
    if (lexical.contains("givenName")) return (givenName);
    if (lexical.contains("givenname")) return (givenname);
    if (lexical.contains("holdsAccount")) return (holdsAccount);
    if (lexical.contains("icqChatID")) return (icqChatID);
    if (lexical.contains("isPrimaryTopicOf")) return (isPrimaryTopicOf);
    if (lexical.contains("jabberID")) return (jabberID);
    if (lexical.contains("lastName")) return (lastName);
    if (lexical.contains("mbox_sha1sum")) return (mbox_sha1sum);
    if (lexical.contains("membershipClass")) return (membershipClass);
    if (lexical.contains("msnChatID")) return (msnChatID);
    if (lexical.contains("myersBriggs")) return (myersBriggs);
    if (lexical.contains("pastProject")) return (pastProject);
    if (lexical.contains("primaryTopic")) return (primaryTopic);
    if (lexical.contains("schoolHomepage")) return (schoolHomepage);
    if (lexical.contains("skypeID")) return (skypeID);
    if (lexical.contains("topic_interest")) return (topic_interest);
    if (lexical.contains("workInfoHomepage")) return (workInfoHomepage);
    if (lexical.contains("workplaceHomepage")) return (workplaceHomepage);
    if (lexical.contains("yahooChatID")) return (yahooChatID);
    if (lexical.contains("depiction")) return (depiction);
    if (lexical.contains("depicts")) return (depicts);
    if (lexical.contains("homepage")) return (homepage);
    if (lexical.contains("interest")) return (interest);
    if (lexical.contains("Organization")) return (Organization);
    if (lexical.contains("organization")) return (Organization);
    if (lexical.contains("focus")) return (focus);
    if (lexical.contains("img")) return (img);
    if (lexical.contains("mbox")) return (mbox);
    if (lexical.contains("member")) return (member);
    if (lexical.contains("Project")) return (Project);
    if (lexical.contains("Agent")) return (Agent);
    if (lexical.contains("Document")) return (Document);
    if (lexical.contains("Group")) return (Group);
    if (lexical.contains("Image")) return (Image);
    if (lexical.contains("account")) return (account);
    if (lexical.contains("logo")) return (logo);
    if (lexical.contains("made")) return (made);
    if (lexical.contains("maker")) return (maker);
    if (lexical.contains("knows")) return (knows);
    if (lexical.contains("nick")) return (nick);
    if (lexical.contains("openid")) return (openid);
    if (lexical.contains("phone")) return (phone);
    if (lexical.contains("plan")) return (plan);
    if (lexical.contains("publications")) return (publications);
    if (lexical.contains("surname")) return (surname);
    if (lexical.contains("thumbnail")) return (thumbnail);
    if (lexical.contains("tipjar")) return (tipjar);
    if (lexical.contains("Person")) return (Person);
    if (lexical.contains("page")) return (page);
    if (lexical.contains("weblog")) return (weblog);
    if (lexical.contains("status")) return (status);
    if (lexical.contains("sha1")) return (sha1);
    if (lexical.contains("theme")) return (theme);
    if (lexical.contains("topic")) return (topic);
    if (lexical.contains("title")) return (title);
    if (lexical.contains("name")) return (name);
    if (lexical.contains("age")) return (age);

    return error;
    // throw new IllegalArgumentException("Can not recognise URI " + lexical);
  }
}
