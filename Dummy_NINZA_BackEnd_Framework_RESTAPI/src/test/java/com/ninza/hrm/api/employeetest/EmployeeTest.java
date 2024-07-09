package com.ninza.hrm.api.employeetest;

import static io.restassured.RestAssured.given;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.Driver;
import com.ninza.hrm.api.pojoclass.EmployeePOJO;
import com.ninza.hrm.api.pojoclass.ProjectPOJO;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class EmployeeTest {   
/*In our TestCase Excel sheet,it is asking us to "verify the status of the Project in GUI" after JDBC program but,this is not done here,bcz here our concentration only on Backend Automation like,API layer & DB layer(WhyBcz we know how to write selenium automation program already(ie,how to launch a browser,how to login to an app,how to search for a project & how to put a validation)*/
	  
	
//TESTCASE#:1(available in Excel sheet)
	@Test
	public void addEmployeeTest() throws Throwable {
		
		Random random=new Random();
		int ranNum = random.nextInt(5000);
		
		String projectName = "Jio_"+ranNum;
		String userName = "user_"+ranNum;
		
		//API-1==>add a project inside the Server
		ProjectPOJO pObj=new ProjectPOJO(projectName, "Created", "GAGAN", 10);
	    given()
		.contentType(ContentType.JSON)
		.body(pObj)
		.when()
		.post("http://49.249.28.218:8091/addProject")
		.then()
		.log().all();
		
         
       //API-2==>add Employee to the same project
         EmployeePOJO empObj=new EmployeePOJO("Arhitect", "24/06/1947", "gogo@gmail.com", userName, 18, "9888000876", projectName, "ROLE_EMPLOYEE", userName); //This class is there in 'com.ninza.hrm.api.pojoclass' package.
         given()
 		.contentType(ContentType.JSON)
 		.body(empObj)
 		.when()
 		.post("http://49.249.28.218:8091/employees")
 		.then()
         .assertThat().statusCode(201)
         .assertThat().contentType(ContentType.JSON)
         .and()
         .time(Matchers.lessThan(3000L))
         .log().all();
         
         
    //Verify Employee name in DB
         boolean flag = false;
 		Driver driverRef=new Driver();
 		DriverManager.registerDriver(driverRef);
 		Connection con = DriverManager.getConnection("jdbc:mysql://49.249.28.218:3333/ninza_hrm","root@%","root");
 		ResultSet result = con.createStatement().executeQuery("select * from employee");
 		while(result.next())
 		{
 			if(result.getString(5).equals(userName))   //Here I'm using 5,bcz inside the database(which was shown by deepak sir already & we don't have an "access" to that DB,but we can only "connect" to DB via program) employeeName/userName column is there as 5th column only. 	
 			{
 				flag=true;
 				break;
 			}
 		}
 		con.close();  //This much program(from line#54-67)is enough to check whether ur project is getting inserted in the DB there or not.
 		Assert.assertTrue(flag,"Employee/User in DB is not verified");  //This msg("Project in DB is not verified") will get display only in case of assertion failure. 
	}
	

	
	
	
	
	//TESTCASE#:2(available in Excel sheet)
	@Test
	public void addEmployeeWithOutEmailTest() {
		
		Random random=new Random();
		int ranNum = random.nextInt(5000);
		
		String projectName = "Jio_"+ranNum;
		String userName = "user_"+ranNum;
		
		//API-1==>add a project inside the Server
		ProjectPOJO pObj=new ProjectPOJO(projectName, "Created", "GAGAN", 10);
	    given()
		.contentType(ContentType.JSON)
		.body(pObj)
		.when()
		.post("http://49.249.28.218:8091/addProject")
		.then()
		.log().all();
		
         
         
       //API-2==>add Employee to the same project
         EmployeePOJO empObj=new EmployeePOJO("Arhitect", "24/06/1947", "", userName, 18, "9888000876", projectName, "ROLE_EMPLOYEE", userName); //This class is there in 'com.ninza.hrm.api.pojoclass' package.
         given()
 		.contentType(ContentType.JSON)
 		.body(empObj)
 		.when()
 		.post("http://49.249.28.218:8091/employees")
 		.then()
         .assertThat().statusCode(500)
         .assertThat().contentType(ContentType.JSON)
         .log().all();
         
         
	}
}