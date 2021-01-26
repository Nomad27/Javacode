package com.mobiquity.assignment;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;

public class UserTestData extends BaseTest {

	private static final Logger logger = LogManager.getLogger(UserTestData.class.getName());

	private static final String USER_NAME = "Delphine";
	
	private List<Integer> userIds = new ArrayList<>();
	private List<Integer> userPostIds = new ArrayList<>();
@BeforeClass
public void setup() {
	
	RestAssured.baseURI = BASE_URI;
}
	
	/** Test method for Search User */ 
	@Test
	public void searchUser() {

		//Put information in log file
		logger.info("Search {} user .", USER_NAME);
		
		//create the test for Extent Report
        extentlogger = extent.createTest("searchUser: Search User as Delphine");
        extentlogger.log(Status.INFO, "Search User as Delphine");
        extentlogger.log(Status.INFO, "Check name and email field exist and do not has null value");
		
        //Fetch the Id for user 
		List<Integer> usrIds = given()
		.param("username", USER_NAME)
		.when().get(BASE_PATH_USERS)
		.then()
		.statusCode(HttpStatus.SC_OK)
		.body("name", notNullValue())
		.body("email",notNullValue())
		.extract()
		.jsonPath()
		.getList("id");
		
		//Checking the user id
		Assert.assertArrayEquals(new Integer[] { 9 }, usrIds.toArray());
		
		userIds = usrIds;
	}

	/** Test method for Search User's Post, This method is depend on the test method searchUser */ 
	@Test(dependsOnMethods = "searchUser")
	public void searchUserPosts() {
		
		//Adding test for Extent Report
		extentlogger = extent.createTest("searchUserPosts: Search User's all post");
		extentlogger.log(Status.INFO, "Search User Delphine's all post");
		extentlogger.log(Status.INFO, "Check userId, id, title and body field exist and do not has null value");
		for(int userId : userIds) {
			
			//Adding information in log file
			logger.info("fetch all the post written by {}.", userId);
			
			//Send request and get the Ids for all the posts
			List<Integer> usrPostIds = given()
			.param("userId", userId)
			.when()
			.get(BASE_PATH_POSTS)
			.then()
			.statusCode(HttpStatus.SC_OK)
			.body("userId", notNullValue())
			.body("id", notNullValue())
			.body("title", notNullValue())
			.body("body", notNullValue())
			.extract()
			.jsonPath()
			.getList("id");
			
			//Assert statement for all the post Ids for user
			Assert.assertArrayEquals(new Integer[] { 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 }, usrPostIds.toArray());
		
			if(usrPostIds != null) {
				userPostIds.addAll(usrPostIds);
			}
		}
		
	}

	/** Test method for Search comments for  User's Post, This method is depend on the test method searchUserPosts */ 
	@Test(dependsOnMethods = "searchUserPosts")
	public void searchCommentsForPostsAndValidateEmail() {
		//Adding test for Extent Report
		extentlogger = extent.createTest("searchCommentsForPostsAndValidateEmail: Search comments for User's all post");
		extentlogger.log(Status.INFO, "Search comments for the user's post");
		
		for(Integer postId : userPostIds) {
			
			extentlogger.log(Status.INFO, "Comments for the post "+postId);
			extentlogger.log(Status.INFO, "Check id, name, email and body field exist and do not has null value");
			extentlogger.log(Status.INFO, "Validate the email format");
			//Adding information in log file
			logger.info("fetch all comments for the post id {}.", postId);
			
			//Send request and fetch the email ids
			List<String> emailList = given()
					.pathParam("postId", postId)
					.when()
					.get(BASE_PATH_POSTS + "/ {postId} /" + BASE_PATH_COMMENTS)
					.then()
					.statusCode(HttpStatus.SC_OK)
					.body("id", notNullValue())
					.body("name", notNullValue())
					.body("email", notNullValue())
					.body("body", notNullValue())
					.extract()
					.jsonPath()
					.getList("email");
	
			//Checking Email Id format 	
			assertEmail(emailList);
		}
	}

	private void assertEmail(List<String> emails) {

		if (emails != null) {
			for (String email : emails) {
				//Assert statement for valid email Id.
				Assert.assertTrue(isValid(email));
			}
		}
	}

	private boolean isValid(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);

		if (email == null)
			return false;

		return pat.matcher(email).matches();
	}

	
}
