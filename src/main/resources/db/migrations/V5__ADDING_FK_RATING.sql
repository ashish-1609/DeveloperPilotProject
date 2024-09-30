ALTER TABLE Rating
    ADD CONSTRAINT FK_Rating_Hotel
        FOREIGN KEY (hotel_id)
            REFERENCES Hotel(id);

ALTER TABLE Rating
    ADD CONSTRAINT FK_Rating_User
        FOREIGN KEY (user_id)
            REFERENCES User(id);