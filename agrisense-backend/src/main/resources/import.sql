-- import.sql placeholder for quarkus.hibernate-orm.sql-load-script

-- If Farmer/Field entities do not use IDENTITY, you can still set IDs manually.
-- Usually it is cleaner to omit IDs. For now, only Sensors are adjusted.

INSERT INTO farmers (id, username, email) VALUES (1, 'john_farmer', 'john@agrisense.io');

INSERT INTO fields (id, name, location, farmer_id) VALUES (1, 'North Field', 'GPS: 40.7128,-74.0060', 1);

-- FIXED: Removed the "id" column and explicit values.
-- The database will now update the sequence automatically.
INSERT INTO sensors (name, api_key, type, field_id) VALUES ('Temperature Sensor 1', 'test-api-key-123', 'TEMPERATURE', 1);
INSERT INTO sensors (name, api_key, type, field_id) VALUES ('Moisture Sensor 1', 'test-api-key-456', 'MOISTURE', 1);