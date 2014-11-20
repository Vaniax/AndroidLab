SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `InstaMeet` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `InstaMeet` ;

-- -----------------------------------------------------
-- Table `InstaMeet`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `InstaMeet`.`User` (
  `userid` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `password` FLOAT NULL,
  `salt` VARCHAR(45) NULL,
  `lattitude` FLOAT NULL,
  `latest_location_update` TIMESTAMP NULL,
  PRIMARY KEY (`userid`),
  UNIQUE INDEX `userid_UNIQUE` (`userid` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `InstaMeet`.`Appointments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `InstaMeet`.`Appointments` (
  `appointmentid` INT NOT NULL AUTO_INCREMENT,
  `hostid` INT NULL,
  `starting_time` TIME NULL,
  `description` TEXT NULL,
  `longitude` FLOAT NULL,
  `lattitude` FLOAT NULL,
  PRIMARY KEY (`appointmentid`),
  UNIQUE INDEX `appointmentid_UNIQUE` (`appointmentid` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `InstaMeet`.`Friends`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `InstaMeet`.`Friends` (
  `friendid` INT NOT NULL,
  `user1` INT NULL,
  `user2` INT NULL,
  PRIMARY KEY (`friendid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `InstaMeet`.`ChatMessages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `InstaMeet`.`ChatMessages` (
  `friendid` INT NOT NULL,
  `message` TEXT NULL,
  `message_date` TIMESTAMP NULL)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
