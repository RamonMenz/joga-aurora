-- Inserir Turmas
INSERT INTO classroom (id, name)
VALUES
    ('classroo-m000-0000-test-000000000011', '1A'),
    ('classroo-m000-0000-test-000000000012', '1B');

-- Inserir Estudantes da Turma 1A
INSERT INTO student (id, name, birth_date, gender, classroom_id) VALUES
    ('student0-0000-0000-test-000000000001', 'Ana Souza', '2015-04-12', 'F', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000002', 'Bruno Lima', '2015-03-09', 'M', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000003', 'Carla Mendes', '2015-06-25', 'F', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000004', 'Daniel Silva', '2015-05-13', 'M', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000005', 'Eduardo Souza', '2015-07-22', 'M', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000006', 'Fernanda Costa', '2015-02-17', 'F', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000007', 'Gustavo Rocha', '2015-01-29', 'M', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000008', 'Helena Martins', '2015-08-09', 'F', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000009', 'Igor Santos', '2015-09-11', 'M', 'classroo-m000-0000-test-000000000011'),
    ('student0-0000-0000-test-000000000010', 'Juliana Alves', '2015-10-01', 'F', 'classroo-m000-0000-test-000000000011');

-- Inserir Estudantes da Turma 1B
INSERT INTO student (id, name, birth_date, gender, classroom_id) VALUES
    ('student0-0000-0000-test-000000000011', 'Alice Barros', '2015-05-05', 'F', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000012', 'Breno Costa', '2015-04-22', 'M', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000013', 'Camila Duarte', '2015-02-11', 'F', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000014', 'Diego Nunes', '2015-01-17', 'M', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000015', 'Elisa Torres', '2015-07-03', 'F', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000016', 'Felipe Santos', '2015-06-14', 'M', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000017', 'Gabriela Prado', '2015-09-01', 'F', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000018', 'Henrique Silva', '2015-03-23', 'M', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000019', 'Isabela Ramos', '2015-08-16', 'F', 'classroo-m000-0000-test-000000000012'),
    ('student0-0000-0000-test-000000000020', 'João Pedro', '2015-10-30', 'M', 'classroo-m000-0000-test-000000000012');

-- Turma 1A
INSERT INTO attendance (id, student_id, attendance_date, status) VALUES
-- Data 1
('attendan-ce00-0000-test-000000000001', 'student0-0000-0000-test-000000000001', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000002', 'student0-0000-0000-test-000000000002', '2025-09-01', 'A'),
('attendan-ce00-0000-test-000000000003', 'student0-0000-0000-test-000000000003', '2025-09-01', 'L'),
('attendan-ce00-0000-test-000000000004', 'student0-0000-0000-test-000000000004', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000005', 'student0-0000-0000-test-000000000005', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000006', 'student0-0000-0000-test-000000000006', '2025-09-01', 'A'),
('attendan-ce00-0000-test-000000000007', 'student0-0000-0000-test-000000000007', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000008', 'student0-0000-0000-test-000000000008', '2025-09-01', 'L'),
('attendan-ce00-0000-test-000000000009', 'student0-0000-0000-test-000000000009', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000010', 'student0-0000-0000-test-000000000010', '2025-09-01', 'P'),

-- Data 2
('attendan-ce00-0000-test-000000000011', 'student0-0000-0000-test-000000000001', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000012', 'student0-0000-0000-test-000000000002', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000013', 'student0-0000-0000-test-000000000003', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000014', 'student0-0000-0000-test-000000000004', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000015', 'student0-0000-0000-test-000000000005', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000016', 'student0-0000-0000-test-000000000006', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000017', 'student0-0000-0000-test-000000000007', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000018', 'student0-0000-0000-test-000000000008', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000019', 'student0-0000-0000-test-000000000009', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000020', 'student0-0000-0000-test-000000000010', '2025-09-02', 'P'),

-- Data 3
('attendan-ce00-0000-test-000000000021', 'student0-0000-0000-test-000000000001', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000022', 'student0-0000-0000-test-000000000002', '2025-09-03', 'A'),
('attendan-ce00-0000-test-000000000023', 'student0-0000-0000-test-000000000003', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000024', 'student0-0000-0000-test-000000000004', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000025', 'student0-0000-0000-test-000000000005', '2025-09-03', 'L'),
('attendan-ce00-0000-test-000000000026', 'student0-0000-0000-test-000000000006', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000027', 'student0-0000-0000-test-000000000007', '2025-09-03', 'A'),
('attendan-ce00-0000-test-000000000028', 'student0-0000-0000-test-000000000008', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000029', 'student0-0000-0000-test-000000000009', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000030', 'student0-0000-0000-test-000000000010', '2025-09-03', 'P'),

-- Data 4
('attendan-ce00-0000-test-000000000031', 'student0-0000-0000-test-000000000001', '2025-09-04', 'A'),
('attendan-ce00-0000-test-000000000032', 'student0-0000-0000-test-000000000002', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000033', 'student0-0000-0000-test-000000000003', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000034', 'student0-0000-0000-test-000000000004', '2025-09-04', 'L'),
('attendan-ce00-0000-test-000000000035', 'student0-0000-0000-test-000000000005', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000036', 'student0-0000-0000-test-000000000006', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000037', 'student0-0000-0000-test-000000000007', '2025-09-04', 'A'),
('attendan-ce00-0000-test-000000000038', 'student0-0000-0000-test-000000000008', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000039', 'student0-0000-0000-test-000000000009', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000040', 'student0-0000-0000-test-000000000010', '2025-09-04', 'P');

-- Turma 1B
INSERT INTO attendance (id, student_id, attendance_date, status) VALUES
-- Data 1
('attendan-ce00-0000-test-000000000041', 'student0-0000-0000-test-000000000011', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000042', 'student0-0000-0000-test-000000000012', '2025-09-01', 'A'),
('attendan-ce00-0000-test-000000000043', 'student0-0000-0000-test-000000000013', '2025-09-01', 'L'),
('attendan-ce00-0000-test-000000000044', 'student0-0000-0000-test-000000000014', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000045', 'student0-0000-0000-test-000000000015', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000046', 'student0-0000-0000-test-000000000016', '2025-09-01', 'A'),
('attendan-ce00-0000-test-000000000047', 'student0-0000-0000-test-000000000017', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000048', 'student0-0000-0000-test-000000000018', '2025-09-01', 'L'),
('attendan-ce00-0000-test-000000000049', 'student0-0000-0000-test-000000000019', '2025-09-01', 'P'),
('attendan-ce00-0000-test-000000000050', 'student0-0000-0000-test-000000000020', '2025-09-01', 'P'),

-- Data 2
('attendan-ce00-0000-test-000000000051', 'student0-0000-0000-test-000000000011', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000052', 'student0-0000-0000-test-000000000012', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000053', 'student0-0000-0000-test-000000000013', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000054', 'student0-0000-0000-test-000000000014', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000055', 'student0-0000-0000-test-000000000015', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000056', 'student0-0000-0000-test-000000000016', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000057', 'student0-0000-0000-test-000000000017', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000058', 'student0-0000-0000-test-000000000018', '2025-09-02', 'P'),
('attendan-ce00-0000-test-000000000059', 'student0-0000-0000-test-000000000019', '2025-09-02', 'A'),
('attendan-ce00-0000-test-000000000060', 'student0-0000-0000-test-000000000020', '2025-09-02', 'P'),

-- Data 3
('attendan-ce00-0000-test-000000000061', 'student0-0000-0000-test-000000000011', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000062', 'student0-0000-0000-test-000000000012', '2025-09-03', 'A'),
('attendan-ce00-0000-test-000000000063', 'student0-0000-0000-test-000000000013', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000064', 'student0-0000-0000-test-000000000014', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000065', 'student0-0000-0000-test-000000000015', '2025-09-03', 'L'),
('attendan-ce00-0000-test-000000000066', 'student0-0000-0000-test-000000000016', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000067', 'student0-0000-0000-test-000000000017', '2025-09-03', 'A'),
('attendan-ce00-0000-test-000000000068', 'student0-0000-0000-test-000000000018', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000069', 'student0-0000-0000-test-000000000019', '2025-09-03', 'P'),
('attendan-ce00-0000-test-000000000070', 'student0-0000-0000-test-000000000020', '2025-09-03', 'P'),

-- Data 4
('attendan-ce00-0000-test-000000000071', 'student0-0000-0000-test-000000000011', '2025-09-04', 'A'),
('attendan-ce00-0000-test-000000000072', 'student0-0000-0000-test-000000000012', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000073', 'student0-0000-0000-test-000000000013', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000074', 'student0-0000-0000-test-000000000014', '2025-09-04', 'L'),
('attendan-ce00-0000-test-000000000075', 'student0-0000-0000-test-000000000015', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000076', 'student0-0000-0000-test-000000000016', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000077', 'student0-0000-0000-test-000000000017', '2025-09-04', 'A'),
('attendan-ce00-0000-test-000000000078', 'student0-0000-0000-test-000000000018', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000079', 'student0-0000-0000-test-000000000019', '2025-09-04', 'P'),
('attendan-ce00-0000-test-000000000080', 'student0-0000-0000-test-000000000020', '2025-09-04', 'P');
