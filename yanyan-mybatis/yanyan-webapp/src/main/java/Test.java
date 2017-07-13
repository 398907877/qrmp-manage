import com.yanyan.core.spring.GsonFactoryBean;
import com.yanyan.core.web.DataResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Saintcy
 * Date: 2015/4/15
 * Time: 20:42
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //invokeService("", "login", null);
        //System.out.println(BeanUtils.describe(new Department()));
        /*String sql = "select * from a where name = :name and parentName = :parentName and staff_id = :s.id and staff_id in (:staff.id) and :staff.id";

        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList( NamedParameterUtils.parseSqlStatement(sql), (SqlParameterSource)(new MapSqlParameterSource(new HashMap<String, Object>())));
        for(SqlParameter parameter:declaredParameters){
            System.out.println(parameter.getName());
        }

        System.out.println(declaredParameters);*/

        GsonFactoryBean bean = new GsonFactoryBean();
        bean.afterPropertiesSet();
        System.out.println(bean.getObject().toJson(DataResponse.success("aasss", 663537474884843993L)));
        System.out.println(System.currentTimeMillis());
        try {
            /*Staff staff = new Staff();
            //staff.setAccount(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
            staff.setPassword("ddddddddddddddddddddddddddddddddddddddddd");
            LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
            validator.afterPropertiesSet();
            BeanValidators.validateWithException(validator, staff, Create.class);*/


            //System.out.println(SystemUtils.getLocalIPs(true));

            //System.out.println(SigarLoader.getNativeLibraryName());
            //System.out.println(sigar.getCpu().getIdle());
            //System.out.println(sigar.getCpu().getIdle());
            /*Gson gson = new Gson();
            Attachment attachment = gson.fromJson("{url: \"abc\"}", Attachment.class);
            attachment.setUrl("ddd");
            System.out.println(attachment.getUrl());
            System.out.println(attachment.getFileUrl());
            System.out.println(gson.toJson(attachment));*/

        } catch (Exception e) {
            System.out.println(DataResponse.failure(e));
        }

        //System.out.println(PinyinUtils.getPinyin("consumes", PinyinUtils.PinyinFormat.WITH_TONE_MARK));


    }


    static void invokeService(String serviceName, String operationName, Object parameter) {
        try {
            String contentType = "application/json; charset=utf-8";
            //contentType = "application/x-www-form-urlencoded; charset=utf-8";
            //contentType = "application/xml; charset=utf-8";
            //contentType = "text/html; charset=utf-8";
            String charset = "utf-8";
            PostMethod post = new PostMethod("http://localhost:8087/money/" + (StringUtils.isNotEmpty(serviceName) ? serviceName + "/" : "") + operationName + ".html");

            String content = "{\"corp_id\": \"999\", \"username\": \"tangss\", \"password\": \"000000\", createDate: \"2015/11/2\"}";//JSONUtils.toJSON(parameter);
            //content = "<user><id>123</id><username>abc</username><password>000000</password></user>";
            /*NameValuePair[] content = new NameValuePair[8];
            NameValuePair id = new NameValuePair();
            id.setName("corp_id");
            id.setValue("999");
            content[0] = id;
            NameValuePair username = new NameValuePair();
            username.setName("username");
            username.setValue("abc");
            content[1] = (username);
            NameValuePair password = new NameValuePair();
            password.setName("password");
            password.setValue(FileUtils.readFileToString(new File("C:\\Users\\Saintcy\\Desktop\\test.txt")));
            //password.setValue("000000");
            content[2] = password;
            NameValuePair corpid = new NameValuePair();
            corpid.setName("staff.id");
            corpid.setValue("121242424");
            content[3] = corpid;

            NameValuePair abc1 = new NameValuePair();
            abc1.setName("abc[0]");
            abc1.setValue("abc1");
            content[4] = abc1;

            NameValuePair abc2 = new NameValuePair();
            abc2.setName("abc[1]");
            abc2.setValue("abc2");
            content[5] = abc2;

            NameValuePair staffAccount = new NameValuePair();
            staffAccount.setName("staff.account");
            staffAccount.setValue("accounttest");
            content[6] = staffAccount;

            NameValuePair staffrole_id = new NameValuePair();
            staffrole_id.setName("staff.role_id");
            staffrole_id.setValue("99999091292");
            content[7] = staffrole_id;*/
            /*Key k = DESUtil.generateKey("shop.elg365.com".getBytes());
            content = new String(DESUtil.encrypt(k, content.getBytes(charset)));
            System.out.println(content);*/
            //post.addRequestHeader("Accept", "application/json");
            post.addRequestHeader("Content-Type", contentType);
            //post.addRequestHeader("Accept", "application/xml");
            //post.addRequestHeader("Accept", "text/html");

            RequestEntity requestEntity = new StringRequestEntity(content, contentType, charset);
            post.setRequestEntity(requestEntity);


            //post.setRequestBody(content);

            HttpClient httpclient = new HttpClient();
            httpclient.getParams().setConnectionManagerTimeout(10000);
            httpclient.getParams().setSoTimeout(60000);

            int result = httpclient.executeMethod(post);
            System.out.println(result);
            System.out.println(post.getResponseBodyAsString());
            //System.out.println(new String(DESUtil.decrypt(k, post.getResponseBodyAsString().getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
