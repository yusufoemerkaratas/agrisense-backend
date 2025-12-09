-- import.sql placeholder for quarkus.hibernate-orm.sql-load-script

-- Farmer ve Field için ID'leri elle vermeye devam edebilirsin (eğer onların entity'sinde IDENTITY yoksa)
-- Ama genellikle ID'leri kaldırmak en temizidir. Şimdilik hatayı çözmek için sadece Sensors'u düzeltiyoruz.

INSERT INTO farmers (id, username, email) VALUES (1, 'john_farmer', 'john@agrisense.io');

INSERT INTO fields (id, name, location, farmer_id) VALUES (1, 'North Field', 'GPS: 40.7128,-74.0060', 1);

-- DÜZELTİLEN KISIM: "id" sütununu ve "1", "2" değerlerini sildik.
-- Artık veritabanı bunları eklerken sayacı da güncelleyecek.
INSERT INTO sensors (name, api_key, type, field_id) VALUES ('Temperature Sensor 1', 'test-api-key-123', 'TEMPERATURE', 1);
INSERT INTO sensors (name, api_key, type, field_id) VALUES ('Moisture Sensor 1', 'test-api-key-456', 'MOISTURE', 1);