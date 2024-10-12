-- Table invoices
CREATE TABLE invoices (
                          id INTEGER PRIMARY KEY AUTO_INCREMENT,
                          number VARCHAR(255) NOT NULL,
                          total DECIMAL(10, 2) NOT NULL,
                          sub_total DECIMAL(10, 2) NOT NULL,
                          taxes DECIMAL(10, 2) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          client_account_number VARCHAR(8) NOT NULL
);

-- Index for client_id
CREATE INDEX idx_client_id ON invoices(client_account_number);

-- Table invoice_details
CREATE TABLE invoice_details (
                                 id INTEGER PRIMARY KEY AUTO_INCREMENT,
                                 product_name VARCHAR(255) NOT NULL,
                                 quantity INTEGER NOT NULL,
                                 price DECIMAL(10, 2) NOT NULL,
                                 total DECIMAL(10, 2) NOT NULL,
                                 invoice_id DECIMAL(10, 0),
                                 FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

-- Index for invoice_id
CREATE INDEX idx_invoice_id ON invoice_details(invoice_id);
