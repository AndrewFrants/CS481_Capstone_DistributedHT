package data;

import java.util.*;
import java.text.*;   // for date and time
import java.net.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class DBInterface {
	AmazonDynamoDB dynamoDB;
	
	private void init() throws Exception {
        /*
         * To configure the Credentials value including the "access_key_id" and "secret_key_id"
         * which could also be found in ./.aws/credentials
         */
		//change the below values to match with your instance 
		  
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAUIHPUI4YQCDDD57X","7ecR7kCa+Ior7Uu6jTa9rPH3xrXIR4FvLaKS/+uL");
		dynamoDB = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .withRegion("us-east-2") //the region of your instance --- the availability zone showed in your EC2 main page 
        .build();

	}
	
	public void insertDocument(String title, String file, String tableName) throws Exception {
		init();
		try {
			// String tableName = tableName;
	        // Create a table with a primary hash key named 'name', which holds a string
	        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
	            .withKeySchema(new KeySchemaElement().withAttributeName("Title").withKeyType(KeyType.HASH))
	            .withAttributeDefinitions(new AttributeDefinition().withAttributeName("Title").withAttributeType(ScalarAttributeType.S))
	            .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(3L).withWriteCapacityUnits(3L));

	        // Create table if it does not exist yet
	        TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
	        // wait for the table to move into ACTIVE state
	        TableUtils.waitUntilActive(dynamoDB, tableName);


	        // Describe our new table
	       // DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
	       // TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
	       // System.out.println("Table Description: " + tableDescription);
	        
	        // Add an item
	        Map<String, AttributeValue> item;
	        item = newItem(title,file);
	        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
	        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
	        System.out.println("Result: " + putItemResult);

	        
			
		}
		catch (AmazonServiceException ase) {
	        System.out.println("Caught an AmazonServiceException, which means your request made it "
	                + "to AWS, but was rejected with an error response for some reason.");
	        System.out.println("Error Message:    " + ase.getMessage());
	        System.out.println("HTTP Status Code: " + ase.getStatusCode());
	        System.out.println("AWS Error Code:   " + ase.getErrorCode());
	        System.out.println("Error Type:       " + ase.getErrorType());
	        System.out.println("Request ID:       " + ase.getRequestId());
	    } catch (AmazonClientException ace) {
	        System.out.println("Caught an AmazonClientException, which means the client encountered "
	                + "a serious internal problem while trying to communicate with AWS, "
	                + "such as not being able to access the network.");
	        System.out.println("Error Message: " + ace.getMessage());
	    }
	}
		
		private Map<String, AttributeValue> newItem(String title, String file) {
		    Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		    item.put("Title", new AttributeValue(title));
		    item.put("File", new AttributeValue(file));
		    return item;
		
	}
	
	

}
