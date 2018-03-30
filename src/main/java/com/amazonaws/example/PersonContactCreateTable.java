package com.amazonaws.example;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class PersonContactCreateTable {
	public static void createTable(AmazonDynamoDB dynamoDB) {
		try {
			String tableName = "personContact";

			ProvisionedThroughput pt = new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L);

			List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("personId").withAttributeType("N"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("addressId").withAttributeType("N"));

			List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName("personId").withKeyType(KeyType.HASH));
			keySchema.add(new KeySchemaElement().withAttributeName("addressId").withKeyType(KeyType.RANGE));

			GlobalSecondaryIndex index = new GlobalSecondaryIndex().withIndexName("personContactIndex")
					.withProvisionedThroughput(pt)
					.withKeySchema(new KeySchemaElement().withAttributeName("addressId").withKeyType(KeyType.HASH))
					.withKeySchema(new KeySchemaElement().withAttributeName("personId").withKeyType(KeyType.RANGE))
					.withProjection(new Projection().withProjectionType(ProjectionType.ALL));

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
					.withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(pt)
					.withGlobalSecondaryIndexes(index);

			System.out.println("Issuing CreateTable request for " + tableName);
			TableUtils.createTableIfNotExists(dynamoDB, request);
			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			TableUtils.waitUntilActive(dynamoDB, tableName);
		} catch (AmazonServiceException ase) {
			System.out.println("Error Message: " + ase.getMessage());
		} catch (AmazonClientException ace) {
			System.out.println("Error Message: " + ace.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Error Message: " + e.getMessage());
		}
	}

}
