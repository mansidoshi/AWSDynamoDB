package com.amazonaws.example;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class AddressCreateTable {
	public static void createTable(AmazonDynamoDB dynamoDB) {
		try {
			String tableName = "address";

			List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("addressId").withAttributeType("N"));

			List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName("addressId").withKeyType(KeyType.HASH));

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
					.withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));

			System.out.println("Issuing CreateTable request for " + tableName);
			TableUtils.createTableIfNotExists(dynamoDB, request);
			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			TableUtils.waitUntilActive(dynamoDB, tableName);

			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
			System.out.println("Table Description: " + tableDescription);

		} catch (AmazonServiceException ase) {
			System.out.println("Error Message: " + ase.getMessage());
		} catch (AmazonClientException ace) {
			System.out.println("Error Message: " + ace.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Error Message: " + e.getMessage());
		}
	}
}
