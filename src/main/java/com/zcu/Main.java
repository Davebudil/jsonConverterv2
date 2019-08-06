package com.zcu;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ini4j.*;
import java.time.*;

public class Main {
    public static void main(String[] args) {
        try {
            //load database settings from .ini file
            Wini ini = new Wini(new File("./dbSettings.ini"));
            String hostName = ini.get("database", "server", String.class);
            Integer port = ini.get("database", "port", Integer.class);
            String dbName = ini.get("database", "databaseName", String.class);
            String user = ini.get("database", "user", String.class);
            String password = ini.get("database","password", String.class);
            String sql;

            String url = String.format("jdbc:sqlserver://%s:%s;database=%s;user=%s;password=%s", hostName, port, dbName, user, password);

            try {
                // Load SQL Server and establish connection.
                System.out.println(url);
                System.out.println("Connecting to Server.");
                try (Connection connection = DriverManager.getConnection(url)) {
                    System.out.println("Done.");

//                     Create a sample database
                    System.out.println("Creating Database.");
                    sql =   "USE [master]\n" +
                            "DROP DATABASE IF EXISTS [patstat018b]; CREATE DATABASE [patstat2018b]\n" +
                            "ALTER DATABASE [patstat018b] SET ANSI_NULL_DEFAULT OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET ANSI_NULLS OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET ANSI_PADDING OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET ANSI_WARNINGS OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET ARITHABORT OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET AUTO_CLOSE OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET AUTO_CREATE_STATISTICS ON \n" +
                            "ALTER DATABASE [patstat2018b] SET AUTO_SHRINK OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET AUTO_UPDATE_STATISTICS ON \n" +
                            "ALTER DATABASE [patstat2018b] SET CURSOR_CLOSE_ON_COMMIT OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET CURSOR_DEFAULT  GLOBAL \n" +
                            "ALTER DATABASE [patstat2018b] SET CONCAT_NULL_YIELDS_NULL OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET NUMERIC_ROUNDABORT OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET QUOTED_IDENTIFIER OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET RECURSIVE_TRIGGERS OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET  DISABLE_BROKER \n" +
                            "ALTER DATABASE [patstat2018b] SET AUTO_UPDATE_STATISTICS_ASYNC OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET DATE_CORRELATION_OPTIMIZATION OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET TRUSTWORTHY OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET ALLOW_SNAPSHOT_ISOLATION OFF \n" +
                            "ALTER DATABASE [patstat2018b] SET PARAMETERIZATION SIMPLE \n" +
                            "ALTER DATABASE [patstat2018b] SET  READ_WRITE \n" +
                            "ALTER DATABASE [patstat2018b] SET RECOVERY FULL \n" +
                            "ALTER DATABASE [patstat2018b] SET  MULTI_USER \n" +
                            "ALTER DATABASE [patstat2018b] SET PAGE_VERIFY CHECKSUM  \n" +
                            "ALTER DATABASE [patstat2018b] SET DB_CHAINING OFF ";
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(sql);
                        System.out.println("Done.");
                    }

                    // Create Tables
                    System.out.println("Creating Tables.");
                    sql =   "USE [patstat2018b] \n" +
                            "CREATE TABLE [dbo].[tls201_appln](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appln_auth] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[appln_nr] [varchar](15)  NOT NULL DEFAULT (''),\n" +
                            "\t[appln_kind] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[appln_filing_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[appln_filing_year] [smallint] NOT NULL DEFAULT '9999',\n" +
                            "\t[appln_nr_epodoc] [varchar](20)  NOT NULL DEFAULT (''),\n" +
                            "\t[appln_nr_original] [varchar](100) NOT NULL DEFAULT (''),\n" +
                            "\t[ipr_type] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[receiving_office] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[internat_appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[int_phase] [char](1) NOT NULL DEFAULT ('N'),\n" +
                            "\t[reg_phase] [char](1) NOT NULL DEFAULT ('N'),\n" +
                            "\t[nat_phase] [char](1) NOT NULL DEFAULT ('N'),\n" +
                            "\t[earliest_filing_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[earliest_filing_year] [smallint] NOT NULL DEFAULT '9999',\n" +
                            "\t[earliest_filing_id] [int] NOT NULL DEFAULT '0',\n" +
                            "\t[earliest_publn_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[earliest_publn_year] [smallint] NOT NULL DEFAULT '9999',\n" +
                            "\t[earliest_pat_publn_id] [int] NOT NULL DEFAULT '0',\n" +
                            "\t[granted] [char](1) NOT NULL DEFAULT ('N'),\n" +
                            "\t[docdb_family_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[inpadoc_family_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[docdb_family_size] [smallint] NOT NULL default '0',\n" +
                            "\t[nb_citing_docdb_fam] [smallint] NOT NULL default '0',\n" +
                            "\t[nb_applicants] [smallint] NOT NULL default '0',\n" +
                            "\t[nb_inventors] [smallint] NOT NULL default '0',\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls202_appln_title](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appln_title_lg] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[appln_title] [nvarchar](max) NOT NULL,\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls203_appln_abstr](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appln_abstract_lg] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[appln_abstract] [nvarchar](max) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls204_appln_prior](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[prior_appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[prior_appln_seq_nr] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[prior_appln_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n\n" +
                            "CREATE TABLE [dbo].[tls205_tech_rel](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[tech_rel_appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[tech_rel_appln_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls206_person](\n" +
                            "\t[person_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[person_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[person_address] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[person_ctry_code] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[doc_std_name_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[doc_std_name] [nvarchar](500)  NOT NULL DEFAULT (''),\n" +
                            "\t[psn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[psn_level] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_sector] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[person_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls207_pers_appln](\n" +
                            "\t[person_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[applt_seq_nr] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[invt_seq_nr] [smallint] NOT NULL DEFAULT ('0')\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[person_id] ASC,\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[applt_seq_nr] ASC,\n" +
                            "\t[invt_seq_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg110_title](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[change_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[title_lg] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[title] [nvarchar](1500) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[change_date] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[title_lg] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg111_licensee](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[change_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[licensee_seq_nr] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[type_license] [char](3) NOT NULL DEFAULT (''),\n" +
                            "\t[designation] [varchar](15) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[valid_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[customer_id] [varchar](20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[name] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[address_1] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[address_2] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[address_3] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[address_4] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[address_5] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[country] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[licensee_seq_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg112_licensee_states](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[licensee_seq_nr] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[licensee_country] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT ('')\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg113_terms_of_grant](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[change_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[lapsed_country] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[lapsed_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[change_date] ASC,\n" +
                            "\t[lapsed_country] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg114_dates](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            " \t[date_type] [varchar](6) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[event_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[cause_interruption] [char](2) NOT NULL DEFAULT ('NA'),\n" +
                            "\t[converted_to_country] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[date_type] ASC,\n" +
                            "\t[event_date] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg117_relation](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[relation_type] [varchar](12) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[child_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[relation_type] ASC,\n" +
                            "\t[child_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg118_prev_filed_appln](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appln_auth] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[appln_nr] [varchar](16) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[appln_date] [date] NOT NULL DEFAULT ('9999-12-31')\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg125_appeal](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[appeal_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[appeal_nr] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[phase] [nvarchar](12) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[date_state_grounds_filed] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[date_interloc_revision] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[result] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[result_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[appeal_date] ASC,\n" +
                            "\t[appeal_nr] ASC,\n" +
                            "\t[date_state_grounds_filed] ASC,\n" +
                            "\t[date_interloc_revision] ASC,\n" +
                            "\t[result] ASC,\n" +
                            "\t[result_date] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg127_petition_rvw](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[review_nr] [nvarchar](15) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[appeal_nr] [nvarchar](25) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[review_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[petitioner_code] [nvarchar](15) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[review_decision_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[review_kind] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT ('')\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[review_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg128_limitation](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[limit_seq_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[limitation_filing_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[limitation_filing_decision] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[date_dispatch_allowance] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[date_payment_allowance] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[date_dispatch_rejection] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[date_legal_effect_rejection] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[limit_seq_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg130_opponent](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[change_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[is_latest] [char](1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[customer_id] [varchar](20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_name] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_address_1] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_address_2] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_address_3] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_address_4] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_address_5] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_country] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[date_opp_filed] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[oppt_status] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[oppt_status_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[agent_name] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_address_1] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_address_2] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_address_3] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_address_4] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_address_5] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[agent_country] [varchar](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\t\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[change_date] ASC,\n" +
                            "\t[oppt_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg135_text](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[change_date] [date] NOT NULL DEFAULT ('99991231'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[text_lg] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[miscellaneous_text] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[text_lg] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg136_search_report](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[office] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[search_type] [char](3) NOT NULL DEFAULT ('0'),\n" +
                            "\t[mailed_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[publn_auth] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[publn_nr] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[publn_kind] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[publn_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[publn_lg] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[bulletin_year] ASC,\n" +
                            "\t[bulletin_nr] ASC,\n" +
                            "\t[publn_kind] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg201_proc_step](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[step_id] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_phase] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[step_code] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_result] [varchar](45) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_result_type] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_country] [char](2) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[time_limit] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[time_limit_unit] [varchar](6) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[step_id] ASC\t\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg202_proc_step_text](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[step_id] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_text] [nvarchar](1000) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_text_type] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[step_id] ASC,\n" +
                            "\t[step_text_type] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg203_proc_step_date](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[step_id] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[step_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[step_date_type] [char](5) NOT NULL DEFAULT ('')\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg301_event_data](\n" +
                            "\t[id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[event_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[event_code] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[bulletin_year] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[bulletin_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[id] ASC,\n" +
                            "\t[event_date] ASC,\n" +
                            "\t[event_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg402_event_text](\n" +
                            "\t[event_code] [varchar](30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "\t[event_text] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[event_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[reg403_appln_status](\n" +
                            "\t[status] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[status_text] [nvarchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[status] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n";
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(sql);
                        System.out.println("Done.");
                    }
//                    Insert Data
                    System.out.println("Inserting Data.");
                    LoadCSV csvLoader = new LoadCSV(connection);
                    String sqlInsert = "INSERT INTO dbo.reg101_appln(id, appln_id, appln_auth, appln_nr, appln_filing_date, filing_lg, status, internat_appln_id, internat_appln_nr, bio_deposit) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    try {
                        csvLoader.insertData("./csvData/reg101_appln.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("1. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg102_pat_publn(id, bulletin_year, bulletin_nr, publn_auth, publn_nr, publn_kind, publn_date, publn_lg) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg102_pat_publn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("2. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg103_ipc(id, ipc_text, change_date, bulletin_year, bulletin_nr) " +
                            "VALUES (?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg103_ipc.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("3. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg106_prior(id, change_date, bulletin_year, bulletin_nr, prior_seq_nr, prior_kind, prior_auth, prior_nr, prior_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg106_prior.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("4. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg107_parties(id, set_seq_nr, is_latest, change_date, bulletin_year, bulletin_nr, type, wishes_to_be_published, seq_nr, designation, customer_id, name, address_1, address_2, address_3, address_4, address_5, country) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg107_parties.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("5. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg108_applicant_states(id, set_seq_nr, bulletin_year, bulletin_nr, type, seq_nr, country) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg108_applicant_states.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("6. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg109_designated_states(id, state_type, change_date, bulletin_year, bulletin_nr, designated_states) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg109_designated_states.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("7. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg110_title(id, change_date, bulletin_year, bulletin_nr, title_lg, title) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg110_title.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("8. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg111_licensee(id, change_date, bulletin_year, bulletin_nr, licensee_seq_nr, type_license, designation, valid_date, customer_id, name, address_1, address_2, address_3, address_4, address_5, country) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg111_licensee.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("9. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg112_licensee_states(id, bulletin_year, bulletin_nr, licensee_seq_nr, licensee_country) " +
                            "VALUES (?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg112_licensee_states.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("10. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg113_terms_of_grant(id, change_date, bulletin_year, bulletin_nr, lapsed_country, lapsed_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg113_terms_of_grant.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("11. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg114_dates(id, bulletin_year, bulletin_nr, date_type, event_date, cause_interruption, converted_to_country) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg114_dates.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("12. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg117_relation(id, child_id, relation_type) " +
                            "VALUES (?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg117_relation.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("13. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg118_prev_filed_appln(id, bulletin_year, bulletin_nr, appln_auth, appln_nr, appln_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg118_prev_filed_appln.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("14. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg125_appeal(id, appeal_date, appeal_nr, phase, date_state_grounds_filed, date_interloc_revision, result, result_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg125_appeal.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("15. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg127_petition_rvw(id, review_nr, appeal_nr, review_date, petitioner_code, review_decision_date, review_kind) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg127_petition_rvw.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("16. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg128_limitation(id, limit_seq_nr, limitation_filing_date, limitation_filing_decision, date_dispatch_allowance, date_payment_allowance, date_dispatch_rejection, date_legal_effect_rejection) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg128_limitation.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("17. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg130_opponent(id, change_date, bulletin_year, bulletin_nr, is_latest, oppt_nr, customer_id, oppt_name, oppt_address_1, oppt_address_2, oppt_address_3, oppt_address_4, oppt_address_5, oppt_country, date_opp_filed, oppt_status, oppt_status_date, agent_name, agent_address_1, agent_address_2, agent_address_3, agent_address_4, agent_address_5, agent_country) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg130_opponent.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("18. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg135_text(id, change_date, bulletin_year, bulletin_nr, text_lg, miscellaneous_text) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg135_text.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("19. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg136_search_report(id, bulletin_year, bulletin_nr, office, search_type, mailed_date, publn_auth, publn_nr, publn_kind, publn_date, publn_lg) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg136_search_report.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("20. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg201_proc_step(id, step_id, step_phase, step_code, step_result, step_result_type, step_country, time_limit, time_limit_unit, bulletin_year, bulletin_nr) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg201_proc_step.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("21. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg202_proc_step_text(id, step_id, step_text, step_text_type) " +
                            "VALUES (?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg202_proc_step_text.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("22. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg203_proc_step_date(id, step_id, step_date, step_date_type) "+
                            "VALUES (?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg203_proc_step_date.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("23. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg301_event_data(id, event_date, event_code, bulletin_year, bulletin_nr, bulletin_date) "+
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg301_event_data.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("24. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg402_event_text(event_code, event_text) "+
                            "VALUES (?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg402_event_text.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("25. Table Done");

                    sqlInsert = "INSERT INTO dbo.reg403_appln_status(status, status_text) "+
                            "VALUES (?, ?)";
                    try {
                        csvLoader.insertData("./csvData/reg403_appln_status.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("26. Table Done");

                    System.out.println("Data import completed.");

                    sql =   "USE register2018b\n" +
                            "SELECT TOP 5\n" +
                            "\t   appln.id 'document.application_id' \n" +
                            "\t  ,appln.appln_id 'application.appln_id'\n" +
                            "      ,appln.appln_auth 'application.appln_auth'\n" +
                            "      ,appln.appln_nr 'application.appln_nr'\n" +
                            "      ,appln.appln_filing_date 'application.appln_filing_date'\n" +
                            "      ,appln.filing_lg 'application.filing_lg'\n" +
                            "      ,appln.status 'application.status'\n" +
                            "      ,appln.internat_appln_id 'application.internat_appln_id'\n" +
                            "      ,appln.internat_appln_nr 'application.internat_appln_nr'\n" +
                            "\t  ,status.status_text 'application.status_text'\n" +
                            "\t  ,(SELECT  \n" +
                            "\t\t   party.name 'party.name'\n" +
                            "\t\t  ,party.set_seq_nr 'party.set_seq_nr'\n" +
                            "\t\t  ,party.is_latest 'party.is_latest'\n" +
                            "\t\t  ,party.change_date 'party.change_date'\n" +
                            "\t\t  ,party.bulletin_year 'party.bulletin_year'\n" +
                            "\t\t  ,party.bulletin_nr 'party.bulletin_nr'\n" +
                            "\t\t  ,party.type 'party.type'\n" +
                            "\t\t  ,party.wishes_to_be_published 'party.wishes_to_be_published'\n" +
                            "\t\t  ,party.seq_nr 'party.seq_nr'\n" +
                            "\t\t  ,party.designation 'party.designation'\n" +
                            "\t\t  ,party.customer_id 'party.customer_id'\n" +
                            "\t\t  ,party.address_1 'party.address_1'\n" +
                            "\t\t  ,party.address_2 'party.address_2'\n" +
                            "\t\t  ,party.address_3 'party.address_3'\n" +
                            "\t\t  ,party.address_4 'party.address_4'\n" +
                            "\t\t  ,party.address_5 'party.address_5'\n" +
                            "\t\t  ,party.country 'party.country'\n" +
                            "\t\t  ,(SELECT \n" +
                            "\t  \t\t\tapplicant_states.type 'applicant.type'\n" +
                            "\t\t\t\t,applicant_states.set_seq_nr 'applicant.set_seq_nr'\n" +
                            "\t\t\t\t,applicant_states.bulletin_year 'applicant.bulletin_year'\n" +
                            "\t\t\t\t,applicant_states.bulletin_nr 'applicant.bulletin_nr'\n" +
                            "\t\t\t\t,applicant_states.seq_nr 'applicant.seq_nr'\n" +
                            "\t\t\t\t,applicant_states.country 'applicant.country'\n" +
                            "\t\t\t\tFROM reg108_applicant_states applicant_states\n" +
                            "\t\t\t\tWHERE applicant_states.id = party.id\n" +
                            "\t\t\t\tFOR JSON PATH) applicant_states\n" +
                            "\t\t  FROM reg107_parties party\n" +
                            "\t\t  WHERE party.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) authors\n" +
                            "\t  ,(SELECT \n" +
                            "\t\t   title.title 'title.title'\n" +
                            "\t\t  ,title.change_date 'title.change_date'\n" +
                            "\t\t  ,title.bulletin_year 'title.bulletin_year'\n" +
                            "\t\t  ,title.bulletin_nr 'title.bulletin_nr'\n" +
                            "\t\t  ,title.title_lg 'title.title_lg'\n" +
                            "\t\t  FROM reg110_title title\n" +
                            "\t\t  WHERE title.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) title\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t   pat_publn.bulletin_year 'abstract.bulletin_year'\n" +
                            "\t\t  ,pat_publn.bulletin_nr 'abstract.bulletin_nr'\n" +
                            "\t\t  ,pat_publn.publn_auth 'abstract.publn_auth'\n" +
                            "\t\t  ,pat_publn.publn_nr 'abstract.publn_nr'\n" +
                            "\t\t  ,pat_publn.publn_kind 'abstract.publn_kind'\n" +
                            "\t\t  ,pat_publn.publn_date 'abstract.publn_date'\n" +
                            "\t\t  ,pat_publn.publn_lg 'abstract.publn_lg'\n" +
                            "\t\t  FROM reg102_pat_publn pat_publn\n" +
                            "\t\t  WHERE pat_publn.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) abstract\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t   ipc.ipc_text 'ipc.ipc_text'\n" +
                            "\t\t  ,ipc.change_date 'ipc.change_date'\n" +
                            "\t\t  ,ipc.bulletin_year 'ipc.bulletin_year'\n" +
                            "\t\t  ,ipc.bulletin_nr 'ipc.bulletin_nr'\n" +
                            "\t\t  FROM reg103_ipc ipc\n" +
                            "\t\t  WHERE ipc.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) ipc\n" +
                            "\t  ,(SELECT \n" +
                            "\t\t   prior.change_date 'prior.change_date'\n" +
                            "\t\t  ,prior.bulletin_year 'prior.bulletin_year'\n" +
                            "\t\t  ,prior.bulletin_nr 'prior.bulletin_nr'\n" +
                            "\t\t  ,prior.prior_seq_nr 'prior.prior_seq_nr'\n" +
                            "\t\t  ,prior.prior_kind 'prior.prior_kind'\n" +
                            "\t\t  ,prior.prior_auth 'prior.prior_auth'\n" +
                            "\t\t  ,prior.prior_nr 'prior.prior_nr'\n" +
                            "\t\t  ,prior.prior_date 'prior.prior_date'\n" +
                            "\t\t  FROM reg106_prior prior\n" +
                            "\t\t  WHERE prior.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) prior\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t   designated_states.state_type 'designated_states.state_type'\n" +
                            "\t\t  ,designated_states.change_date 'designated_states.change_date'\n" +
                            "\t\t  ,designated_states.bulletin_year 'designated_states.bulletin_year'\n" +
                            "\t\t  ,designated_states.bulletin_nr 'designated_states.bulletin_nr'\n" +
                            "\t\t  ,designated_states.designated_states 'designated_states.designated_states'\n" +
                            "\t\t  FROM reg109_designated_states designated_states\n" +
                            "\t\t  WHERE designated_states.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) designated_states\n" +
                            "\t\t  \t  ,(SELECT\n" +
                            "\t\t   licensee.change_date 'licensee.change_date'\n" +
                            "\t\t  ,licensee.bulletin_year 'licensee.bulletin_year'\n" +
                            "\t\t  ,licensee.bulletin_nr 'licensee.bulletin_nr'\n" +
                            "\t\t  ,licensee.licensee_seq_nr 'licensee.licensee_seq_nr'\n" +
                            "\t\t  ,licensee.type_license 'licensee.type_license'\n" +
                            "\t\t  ,licensee.designation 'licensee.designation'\n" +
                            "\t\t  ,licensee.valid_date 'licensee.valid_date'\n" +
                            "\t\t  ,licensee.customer_id 'licensee.customer_id'\n" +
                            "\t\t  ,licensee.name 'licensee.name'\n" +
                            "\t\t  ,licensee.address_1 'licensee.address_1'\n" +
                            "\t\t  ,licensee.address_2 'licensee.address_2'\n" +
                            "\t\t  ,licensee.address_3 'licensee.address_3'\n" +
                            "\t\t  ,licensee.address_4 'licensee.address_4'\n" +
                            "\t\t  ,licensee.address_5 'licensee.address_5'\n" +
                            "\t\t  ,licensee.country 'licensee.country'\n" +
                            "\t\t  ,(SELECT\n" +
                            "\t\t\t\t licensee_states.bulletin_year 'licensee_states.bulletin_year'\n" +
                            "\t\t\t    ,licensee_states.bulletin_nr 'licensee_states.bulletin_nr'\n" +
                            "\t\t\t    ,licensee_states.licensee_seq_nr 'licensee_states.licensee_seq_nr'\n" +
                            "\t\t\t    ,licensee_states.licensee_country 'licensee_states.licensee_country'\n" +
                            "\t\t\t\tFROM reg112_licensee_states licensee_states\n" +
                            "\t\t\t\tWHERE licensee.id = licensee_states.id\n" +
                            "\t\t\t\tFOR JSON PATH) licensee_states\n" +
                            "\t\t  FROM reg111_licensee licensee\n" +
                            "\t\t  WHERE licensee.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) licensee\n" +
                            "\t,(SELECT\n" +
                            "\t\tterms_of_grant.change_date 'terms_of_grant.change_date'\n" +
                            "\t\t,terms_of_grant.bulletin_year 'terms_of_grant.bulletin_year'\n" +
                            "        ,terms_of_grant.bulletin_nr 'terms_of_grant.bulletin_nr'\n" +
                            "\t\t,terms_of_grant.lapsed_country 'terms_of_grant.lapsed_country'\n" +
                            "\t    ,terms_of_grant.lapsed_date 'terms_of_grant.lapsed_date'\n" +
                            "\t\tFROM reg113_terms_of_grant terms_of_grant\n" +
                            "\t\tWHERE terms_of_grant.id = appln.id\n" +
                            "\t\tFOR JSON PATH) terms_of_grant\n" +
                            "\t,(SELECT\n" +
                            "\t\tdates.bulletin_year 'dates.bulletin_year'\n" +
                            "\t\t,dates.bulletin_nr 'dates.bulletin_nr'\n" +
                            "\t\t,dates.date_type 'dates.date_type'\n" +
                            "\t\t,dates.event_date 'dates.event_date'\n" +
                            "\t\t,dates.cause_interruption 'dates.cause_interruption'\n" +
                            "\t\t,dates.converted_to_country 'dates.converted_to_country'\n" +
                            "\t\tFROM reg114_dates dates\n" +
                            "\t\tWHERE dates.id = appln.id\n" +
                            "\t\tFOR JSON PATH) dates\n" +
                            "\t,(SELECT\n" +
                            "\t\trelation.relation_type 'relation.relation_type'\n" +
                            "      ,relation.child_id 'relation.child_id'\n" +
                            "\t  FROM reg117_relation relation\n" +
                            "\t  WHERE relation.id = appln.id\n" +
                            "\tFOR JSON PATH) relation\n" +
                            "    ,(SELECT\n" +
                            "\t   prev_filed_appln.id 'prev_filled_appln.appln_id'\n" +
                            "\t  ,prev_filed_appln.bulletin_year 'prev_filled_appln.appln_bulletin_year'\n" +
                            "      ,prev_filed_appln.bulletin_nr 'prev_filled_appln.appln_bulletin_nr'\n" +
                            "      ,prev_filed_appln.appln_auth 'prev_filled_appln.appln_appln_auth'\n" +
                            "      ,prev_filed_appln.appln_nr 'prev_filled_appln.appln_appln_nr'\n" +
                            "      ,prev_filed_appln.appln_date 'prev_filled_appln.appln_appln_date'\n" +
                            "\t  FROM reg118_prev_filed_appln prev_filed_appln\n" +
                            "\t  WHERE prev_filed_appln.id = appln.id\n" +
                            "\t  FOR JSON PATH) prev_filled_appln\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t appeal.appeal_date 'appeal.appeal_date'\n" +
                            "\t\t,appeal.appeal_nr 'appeal.appeal_nr'\n" +
                            "\t\t,appeal.phase 'appeal.phase'\n" +
                            "\t\t,appeal.date_state_grounds_filed 'appeal.date_state_grounds_filed'\n" +
                            "\t\t,appeal.date_interloc_revision 'appeal.date_interloc_revision'\n" +
                            "\t\t,appeal.result 'appeal.result'\n" +
                            "\t\t,appeal.result_date 'appeal.result_date'\n" +
                            "\t  FROM reg125_appeal appeal\n" +
                            "\t  WHERE appeal.id = appln.id\n" +
                            "\t  FOR JSON PATH) appeal\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t   petition_rvw.review_nr 'petition.review_nr'\n" +
                            "\t\t  ,petition_rvw.appeal_nr 'petition.appeal_nr'\n" +
                            "\t\t  ,petition_rvw.review_date 'petition.review_date'\n" +
                            "\t\t  ,petition_rvw.petitioner_code 'petition.petitioner_code'\n" +
                            "\t\t  ,petition_rvw.review_decision_date 'petition.review_decision_date'\n" +
                            "\t\t  ,petition_rvw.review_kind 'petition.review_kind'\n" +
                            "\t\tFROM reg127_petition_rvw petition_rvw\n" +
                            "\t\tWHERE petition_rvw.id = appln.id\n" +
                            "\t\tFOR JSON PATH) petition_review\n" +
                            "\t  ,(SELECT\n" +
                            "\t\t   limitation.limit_seq_nr 'limitation.limit_seq_nr'\n" +
                            "\t\t  ,limitation.limitation_filing_date 'limitation.limitation_filing_date'\n" +
                            "\t\t  ,limitation.limitation_filing_decision 'limitation.limitation_filing_decision'\n" +
                            "\t\t  ,limitation.date_dispatch_allowance 'limitation.date_dispatch_allowance'\n" +
                            "\t\t  ,limitation.date_payment_allowance 'limitation.date_payment_allowance'\n" +
                            "\t\t  ,limitation.date_dispatch_rejection 'limitation.date_dispatch_rejection'\n" +
                            "\t\t  ,limitation.date_legal_effect_rejection 'limitation.date_legal_effect_rejection'\n" +
                            "\t\tFROM reg128_limitation limitation\n" +
                            "\t\tWHERE limitation.id = appln.id\n" +
                            "\t\tFOR JSON PATH) limitation\n" +
                            "\t\t,(SELECT\n" +
                            "\t\t   opponent.change_date 'opponent.change_date'\n" +
                            "\t\t  ,opponent.bulletin_year 'opponent.bulletin_year'\n" +
                            "\t\t  ,opponent.bulletin_nr 'opponent.bulletin_nr'\n" +
                            "\t\t  ,opponent.is_latest 'opponent.is_latest'\n" +
                            "\t\t  ,opponent.oppt_nr 'opponent.oppt_nr'\n" +
                            "\t\t  ,opponent.customer_id 'opponent.customer_id'\n" +
                            "\t\t  ,opponent.oppt_name 'opponent.oppt_name'\n" +
                            "\t\t  ,opponent.oppt_address_1 'opponent.oppt_address_1'\n" +
                            "\t\t  ,opponent.oppt_address_2 'opponent.oppt_address_2' \n" +
                            "\t\t  ,opponent.oppt_address_3 'opponent.oppt_address_3' \n" +
                            "\t\t  ,opponent.oppt_address_4 'opponent.oppt_address_4' \n" +
                            "\t\t  ,opponent.oppt_address_5 'opponent.oppt_address_5' \n" +
                            "\t\t  ,opponent.oppt_country 'opponent.oppt_country' \n" +
                            "\t\t  ,opponent.date_opp_filed 'opponent.date_opp_filed'\n" +
                            "\t\t  ,opponent.oppt_status 'opponent.oppt_status'\n" +
                            "\t\t  ,opponent.oppt_status_date 'opponent.oppt_status_date'\n" +
                            "\t\t  ,opponent.agent_name 'opponent.agent_name'\n" +
                            "\t\t  ,opponent.agent_address_1 'opponent.agent_address_1'\n" +
                            "\t\t  ,opponent.agent_address_2 'opponent.agent_address_2'  \n" +
                            "\t\t  ,opponent.agent_address_3 'opponent.agent_address_3'\n" +
                            "\t\t  ,opponent.agent_address_4 'opponent.agent_address_4' \n" +
                            "\t\t  ,opponent.agent_address_5 'opponent.agent_address_5' \n" +
                            "\t\t  ,opponent.agent_country 'opponent.agent_country' \n" +
                            "\t\t  FROM reg130_opponent opponent\n" +
                            "\t\t  WHERE opponent.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) opponent\n" +
                            "\t\t  ,(SELECT\n" +
                            "\t\t\t  text.change_date 'text.change_date'\n" +
                            "\t\t\t ,text.bulletin_year 'text.bulletin_year'\n" +
                            "\t\t\t ,text.bulletin_nr 'text.bulletin_nr'\n" +
                            "\t\t\t ,text.text_lg 'text.text_lg'\n" +
                            "\t\t\t ,text.miscellaneous_text 'text.miscellaneous_text'\n" +
                            "\t\t  FROM reg135_text text\n" +
                            "\t\t  WHERE text.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) text\n" +
                            "\t\t  ,(SELECT\n" +
                            "\t\t\t   search_report.bulletin_year 'search_reportbulletin_year'\n" +
                            "\t\t\t  ,search_report.bulletin_nr 'search_reportbulletin_nr'\n" +
                            "\t\t\t  ,search_report.office 'search_reportoffice'\n" +
                            "\t\t\t  ,search_report.search_type 'search_reportsearch_type'\n" +
                            "\t\t\t  ,search_report.mailed_date 'search_reportmailed_date'\n" +
                            "\t\t\t  ,search_report.publn_auth 'search_reportpubln_auth'\n" +
                            "\t\t\t  ,search_report.publn_nr 'search_reportpubln_nr'\n" +
                            "\t\t\t  ,search_report.publn_kind 'search_reportpubln_kind'\n" +
                            "\t\t\t  ,search_report.publn_date 'search_reportpubln_date'\n" +
                            "\t\t\t  ,search_report.publn_lg 'search_reportpubln_lg'\n" +
                            "\t\t  FROM reg136_search_report search_report\n" +
                            "\t\t  WHERE search_report.id = appln.id\n" +
                            "\t\t  FOR JSON PATH) search_report\n" +
                            "\t\t  ,(SELECT\n" +
                            "\t\t\t  proc_step.step_id 'proc_step.step_id'\n" +
                            "\t\t\t  ,proc_step.step_phase 'proc_step.step_phase'\n" +
                            "\t\t\t  ,proc_step.step_code 'proc_step.step_code'\n" +
                            "\t\t\t  ,proc_step.step_result 'proc_step.step_result'\n" +
                            "\t\t\t  ,proc_step.step_result_type 'proc_step.step_result_type' \n" +
                            "\t\t\t  ,proc_step.step_country 'proc_step.step_country'\n" +
                            "\t\t\t  ,proc_step.time_limit 'proc_step.time_limit' \n" +
                            "\t\t\t  ,proc_step.time_limit_unit 'proc_step.time_limit_unit'\n" +
                            "\t\t\t  ,proc_step.bulletin_year 'proc_step.bulletin_year'\n" +
                            "\t\t\t  ,proc_step.bulletin_nr 'proc_step.bulletin_nr'\n" +
                            "\t\t\t  ,(SELECT  \n" +
                            "\t\t\t\t\tproc_step_text.step_id 'proc_step_text.text_step_id'\n" +
                            "\t\t\t\t   ,proc_step_text.step_text 'proc_step_text.text_step_text'\n" +
                            "\t\t\t\t   ,proc_step_text.step_text_type 'proc_step_text.text_step_text_type'\n" +
                            "\t\t\t\t  FROM reg202_proc_step_text proc_step_text\n" +
                            "\t\t\t\t  WHERE proc_step_text.id = proc_step.id\n" +
                            "\t\t\t\t  FOR JSON PATH) proc_step_text\n" +
                            "\t\t\t   ,(SELECT\n" +
                            "\t\t\t\t\tproc_step_date.step_id 'proc_step_date.step_id'\n" +
                            "\t\t\t\t   ,proc_step_date.step_date 'proc_step_date.step_date'\n" +
                            "\t\t\t\t   ,proc_step_date.step_date_type 'proc_step_date.step_date_type'\n" +
                            "\t\t\t\t   FROM reg203_proc_step_date proc_step_date\n" +
                            "\t\t\t\t   WHERE proc_step_date.id = proc_step.id\n" +
                            "\t\t\t\t  FOR JSON PATH) proc_step_date\n" +
                            "\t\t\tFROM reg201_proc_step proc_step\n" +
                            "\t\t\tWHERE proc_step.id = appln.id\n" +
                            "\t\t\tFOR JSON PATH) proc_step\n" +
                            "\t\t,(SELECT \n" +
                            "\t\t\tevent_data.event_date 'event_data.event_date'\n" +
                            "\t\t\t,event_data.event_code 'event_data.event_code' \n" +
                            "\t\t\t,event_data.bulletin_year 'event_data.bulletin_year'\n" +
                            "\t\t\t,event_data.bulletin_nr 'event_data.bulletin_nr'\n" +
                            "\t\t\t,event_data.bulletin_date 'event_data.bulletin_date'\n" +
                            "\t\t\t,(SELECT\n" +
                            "\t\t\t\tevent_text.event_text 'event_text.event_text'\n" +
                            "\t\t\t FROM reg402_event_text event_text\n" +
                            "\t\t\t WHERE event_data.event_code = event_text.event_code\n" +
                            "\t\t\t FOR JSON PATH) event_text\n" +
                            "\t\t\tFROM reg301_event_data event_data\n" +
                            "\t\t\tWHERE event_data.id = appln.id\n" +
                            "\t\t\tFOR JSON PATH) event_data\t\t\t\n" +
                            "  FROM reg101_appln as appln\n" +
                            "  LEFT JOIN reg403_appln_status status on appln.status = status.status\n" +
                            "  FOR JSON PATH, ROOT('PATSTAT')\n";

                    try (Statement statement = connection.createStatement()){
                        System.out.println("Starting String export.");
                        ResultSet rs = statement.executeQuery(sql);
                        PrintWriter writer = new PrintWriter("./convertedData/test.json", "UTF-8");
                        String editedJson = "";
                        String jsonText = "";

                        Instant start = Instant.now();

                        while(rs.next()){
                            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                                jsonText = new StringBuilder().append(jsonText).append(rs.getString(i)).toString();
                            }
                        }

                        GsonBuilder builder = new GsonBuilder();
                        builder.disableHtmlEscaping();
                        Gson gson = builder.setPrettyPrinting().create();
                        JsonParser jParser = new JsonParser();
                        JsonElement jElement = jParser.parse(jsonText);
                        editedJson = gson.toJson(jElement);

                        writer.print(editedJson);
                        writer.close();


                        Instant finish = Instant.now();
                        long timeElapsed = Duration.between(start, finish).toMillis();

                        System.out.println(timeElapsed);
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    connection.close();
                    System.out.println("All done.");
                }
            } catch (Exception e) {
                System.out.println();
                e.printStackTrace();
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}