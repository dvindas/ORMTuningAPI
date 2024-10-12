-- Invoices
    INSERT INTO invoices (id, number, total, sub_total, taxes, created_at, client_account_number) VALUES
    (1, '12375', 330.00, 280.00, 50.00, CURRENT_TIMESTAMP, 'ZXY-234'),
    (2, '12376', 550.00, 490.00, 60.00, CURRENT_TIMESTAMP, 'ZXY-234'),
    (3, '12377', 600.00, 520.00, 80.00, CURRENT_TIMESTAMP, 'ZXY-234'),
    (4, '12378', 660.00, 600.00, 60.00, CURRENT_TIMESTAMP, 'ZXY-234'),
    (5, '12379', 720.00, 640.00, 80.00, CURRENT_TIMESTAMP, 'ZXY-234');

-- Details
INSERT INTO invoice_details (id, product_name, quantity, price, total, invoice_id) VALUES
                                                                                       (1, 'Product A', 2, 100.00, 200.00, 1),
                                                                                       (2, 'Product B', 2, 40.00, 80.00, 1),

                                                                                       (3, 'Product C', 1, 300.00, 300.00, 2),
                                                                                       (4, 'Product D', 2, 95.00, 190.00, 2),   -

                                                                                       (5, 'Product E', 2, 250.00, 500.00, 3),
                                                                                       (6, 'Product F', 1, 20.00, 20.00, 3),

                                                                                       (7, 'Product G', 3, 200.00, 600.00, 4),

                                                                                       (8, 'Product H', 4, 150.00, 600.00, 5),
                                                                                       (9, 'Product I', 2, 20.00, 40.00, 5);    
