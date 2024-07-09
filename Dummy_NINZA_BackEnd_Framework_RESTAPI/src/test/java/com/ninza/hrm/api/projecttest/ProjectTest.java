package com.ninza.hrm.api.projecttest;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.Driver;
import com.ninza.hrm.api.pojoclass.ProjectPOJO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ProjectTest {
/*In our TestCase Excel sheet,it is asking us to "verify the status of the Project in GUI" after JDBC program but,this is not done here,bcz here our concentration only on Backend Automation like,API layer & DB layer(WhyBcz we know how to write selenium automation program already(ie,how to launch a browser,how to login to an app,how to search for a project & how to put a validation)*/
	  
	ProjectPOJO pObj; //This we made it Global,so that we can access the same variable in my 2nd TestCase also.
	
//TESTCASE#:1(available in Excel sheet)
	@Test 
	public void addSingleProjectWithCreatedStatusTest() throws Throwable {

		Random random=new Random();
		int ranNum = random.nextInt(5000);

		String expMsg = "Successfully Added";
		String projectName = "ABCDE_"+ranNum;

	    pObj=new ProjectPOJO(projectName, "Created", "GAGAN", 10);

		//Verify the projectName in API layer
		Response resp = given()
						.contentType(ContentType.JSON)
						.body(pObj)
						.when()
						.post("http://49.249.28.218:8091/addProject");

		resp.then()
		.assertThat().statusCode(201)
		.assertThat().time(Matchers.lessThan(3000L))
		.assertThat().contentType(ContentType.JSON)
		.log().all();

		String actMsg = resp.jsonPath().get("msg");
		Assert.assertEquals(expMsg, actMsg);

		
		

		//Verify the projectName in DB layer
		boolean flag = false;
		Driver driverRef=new Driver();
		DriverManager.registerDriver(driverRef);
		Connection con = DriverManager.getConnection("jdbc:mysql://49.249.28.218:3333/ninza_hrm","root@%","root");
		ResultSet result = con.createStatement().executeQuery("select * from project");
		while(result.next())
		{
			if(result.getString(4).equals(projectName))   //Here I'm using 4,bcz inside the database(which was shown by deepak sir already & we don't have an "access" to that DB,but we can only "connect" to DB via program)projectName column is there as 4th column only. 	
			{
				flag=true;
				break;
			}
		}
		con.close();  //This much program(from line#54-67)is enough to check whether ur project is getting inserted in the DB there or not.
		Assert.assertTrue(flag,"Project in DB is not verified");  //This msg("Project in DB is not verified")of 2nd argument will get display only in case of assertion failure. 

	}
	
	
	
	
	
	
//TESTCASE#:2(available in Excel sheet)	
	@Test(dependsOnMethods = "addSingleProjectWithCreatedStatusTest") //In order to avoid multiple lines of codes,I'm creating a Dependency for 1st Testcase,so that I can achieve CodeOptimization(So.I need not to write the same program for Creating a Project here).
	public void addDuplicateProjectTest() {
		given()
		.contentType(ContentType.JSON)
		.body(pObj)
		.when()
		.post("http://49.249.28.218:8091/addProject")
		.then()
		.assertThat().statusCode(409)
		.log().all();
	}
}
