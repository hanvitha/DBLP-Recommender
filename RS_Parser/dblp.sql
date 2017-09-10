-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dblp3
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dblp3
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dblp3` DEFAULT CHARACTER SET utf8 ;
USE `dblp3` ;

-- -----------------------------------------------------
-- Table `dblp3`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`author` (
  `name` INT(11) NULL DEFAULT NULL,
  `paper_key` INT(11) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `dblp3`.`author_id`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`author_id` (
  `author_name_id` INT(11) NOT NULL,
  `author_name` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`author_name_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dblp3`.`citation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`citation` (
  `paper_cite_key` INT(11) NULL DEFAULT NULL,
  `paper_cited_key` VARCHAR(200) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `dblp3`.`conf_id`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`conf_id` (
  `conf_name_id` INT(11) NOT NULL,
  `conf_name` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`conf_name_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dblp3`.`conference`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`conference` (
  `conf_key` VARCHAR(200) NULL DEFAULT NULL,
  `name` INT(11) NULL DEFAULT NULL,
  `detail` TEXT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `dblp3`.`paper`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`paper` (
  `title` VARCHAR(500) NULL DEFAULT NULL,
  `year` INT(11) NULL DEFAULT '0',
  `conference` INT(11) NULL DEFAULT NULL,
  `paper_key` INT(11) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `dblp3`.`paper_id`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dblp3`.`paper_id` (
  `paper_key_id` INT(11) NOT NULL,
  `paper_key` VARCHAR(300) NULL DEFAULT NULL,
  PRIMARY KEY (`paper_key_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
