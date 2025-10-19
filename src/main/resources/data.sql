-- data.sql
MERGE INTO PRODUCT (id, name, description, price, category) KEY(id) VALUES
  (1, 'VNE Cargo Pants', 'Techwear cargo pants with waterproof fabric', 129.90, 'pants'),
  (2, 'VNE Utility Jacket', 'Lightweight windproof shell', 199.00, 'outerwear'),
  (3, 'VNE Tee Alpha', 'Oversized tee', 39.99, 'tops');

MERGE INTO PRODUCT_SIZES (product_id, size) KEY(product_id, size) VALUES
  (1, 'S'), (1, 'M'), (1, 'L'),
  (2, 'M'), (2, 'L'), (2, 'XL'),
  (3, 'M'), (3, 'L');
