package sbat.logist.ru.parser.exchanger;

import org.springframework.stereotype.Component;
import sbat.logist.ru.parser.json.Point;

import java.util.List;

@Component
public class PointUpdater {
    public void execute(List<Point> points) {


//        CREATE TABLE points
//(
//    pointID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
//    pointIDExternal VARCHAR(128) NOT NULL,
//    dataSourceID VARCHAR(32) NOT NULL,
//    pointName VARCHAR(128) NOT NULL,
//    region VARCHAR(128),
//    timeZone TINYINT(4),
//    docs TINYINT(4),
//    comments LONGTEXT,
//    openTime TIME,
//    closeTime TIME,
//    district VARCHAR(64),
//    locality VARCHAR(64),
//    mailIndex VARCHAR(6),
//    address TEXT NOT NULL,
//    email VARCHAR(255),
//    phoneNumber VARCHAR(255),
//    responsiblePersonId VARCHAR(128),
//    pointTypeID VARCHAR(32) NOT NULL,
//    CONSTRAINT points_ibfk_1 FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
//    CONSTRAINT points_ibfk_2 FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID) ON UPDATE CASCADE
//);
//CREATE INDEX dataSourceID ON points (dataSourceID);
//CREATE UNIQUE INDEX pointIDExternal ON points (pointIDExternal, dataSourceID);
//CREATE INDEX pointTypeID ON points (pointTypeID);
    }
}
