INSERT INTO Author (first_name, last_name, birth_date) VALUES
('Pieter', 'Aspe', '1953-04-03'),
('Dimitri', 'Verhulst', '1972-10-02'),
('Ward', 'Ruyslinck', '1929-06-17'),
('Hendrik', 'Conscience', '1812-12-03'),
('Tom', 'Lanoye', '1958-08-27');

INSERT INTO Book (title, author_firstname, author_lastname, publisher, isbn, release_year, updated_at, created_at) VALUES
('The Square of Revenge', 'Pieter', 'Aspe', 'Pegasus Books', '9781605985619', '1995', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('De Vijfde Macht', 'Pieter', 'Aspe', 'Manteau', '9789022330296 ', '2014', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Vagevuur', 'Pieter', 'Aspe', 'Manteau Thriller', '9789022329054', '2013', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Problemski Hotel', 'Dimitri', 'Verhulst', 'Contact', '9789025425043', '2003', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('De helaasheid der dingen', 'Dimitri', 'Verhulst', 'Atlas Contact', '9789025443689', '2014', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('The deadbeats', 'Ward', 'Ruyslinck', 'Owen', '9780720622256', '1968', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Golden Ophelia', 'Ward', 'Ruyslinck', 'Wpg Uitgevers', '9789022301692', '1966', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Stille waters', 'Ward', 'Ruyslinck', 'Wpg Uitgevers', '9789022310724', '1838', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('De leeuw van Vlaanderen', 'Hendrik', 'Conscience', 'Astoria', '9789491618475', '1838', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Sprakeloos', 'Tom', 'Lanoye', 'Prometheus', '9789044633412', '2017', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Kartonnen dozen', 'Tom', 'Lanoye', 'Prometheus', '9789044621235', '2018', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Het derde huwelijk', 'Tom', 'Lanoye', 'Prometheus', '9789044619997 ', '2012 ', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Heldere hemel', 'Tom', 'Lanoye', 'Prometheus', '9789059651494', '2012', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());



INSERT INTO book_genres (book_id, genres) VALUES
('1', 'Fictie'),
('1', 'Mysterie'),
('2', 'Fictie'),
('2', 'Thriller'),
('3', 'Thriller'),
('4', 'Fictie'),
('5', 'Fictie'),
('5', 'Humor'),
('7', 'Psychologische Roman'),
('7', 'Sociale Roman'),
('9', 'Historische roman'),
('10', 'Fictie'),
('11', 'Biografie'),
('11', 'Psychologische Roman'),
('12', 'Fictie'),
('13', 'Novelle');



