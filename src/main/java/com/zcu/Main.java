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
                            "\t[appln_id] ASC\n" +
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
                            ") ON [PRIMARY]\n" +
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
                            "\t[nuts] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[nuts_level] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[doc_std_name_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[doc_std_name] [nvarchar](500)  NOT NULL DEFAULT (''),\n" +
                            "\t[psn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[psn_level] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_sector] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[han_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[han_name] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[han_harmonized] [int] NOT NULL DEFAULT ('0'),\n" +
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
                            "CREATE TABLE [dbo].[tls209_appln_ipc](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[ipc_class_symbol] [varchar](15) NOT NULL DEFAULT (''),\n" +
                            "\t[ipc_class_level] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[ipc_version] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[ipc_value] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[ipc_position] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[ipc_gener_auth] [char](2) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[ipc_class_symbol] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls210_appln_n_cls](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[nat_class_symbol] [nvarchar](15) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[nat_class_symbol] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = ON) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls211_pat_publn](\n" +
                            "\t[pat_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[publn_auth] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[publn_nr] [varchar](15) NOT NULL DEFAULT (''),\n" +
                            "\t[publn_nr_original] [varchar](100) NOT NULL DEFAULT (''),\n" +
                            "\t[publn_kind] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[publn_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[publn_lg] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[publn_first_grant] [char](1) NOT NULL DEFAULT ('N'),\n" +
                            "\t[publn_claims] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[pat_publn_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls212_citation](\n" +
                            "\t[pat_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_replenished] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_id] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_origin] [char](3) NOT NULL DEFAULT (''),\n" +
                            "\t[cited_pat_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[cited_appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[pat_citn_seq_nr] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[cited_npl_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[npl_citn_seq_nr] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_gener_auth] [char](2) NOT NULL DEFAULT (''),\t\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[pat_publn_id] ASC,\n" +
                            "\t[citn_replenished] ASC,\n" +
                            "\t[citn_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls214_npl_publn](\n" +
                            "\t[npl_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[npl_type] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_biblio] [nvarchar](max) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_author] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_title1] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_title2] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_editor] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_volume] [nvarchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_issue] [nvarchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_publn_date] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_publn_end_date] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_publisher] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_page_first] [varchar](200) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_page_last] [varchar](200) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_abstract_nr] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_doi] [varchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_isbn] [varchar](30) NOT NULL DEFAULT (''),\n" +
                            "\t[npl_issn] [varchar](30) NOT NULL DEFAULT (''),\n" +
                            "\t[online_availability] [varchar](500) NOT NULL DEFAULT (''),\t\n" +
                            "\t[online_classification] [varchar](35) NOT NULL DEFAULT (''),\n" +
                            "\t[online_search_date] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[npl_publn_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls215_citn_categ](\n" +
                            "\t[pat_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_replenished] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_id] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[citn_categ] [nchar](1) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[pat_publn_id] ASC,\n" +
                            "\t[citn_replenished] ASC,\n" +
                            "\t[citn_id] ASC,\n" +
                            "\t[citn_categ] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls216_appln_contn](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[parent_appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[contn_type] [char](3) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[APPLN_ID] ASC,\n" +
                            "\t[PARENT_APPLN_ID] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls222_appln_jp_class](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[jp_class_scheme] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[jp_class_symbol] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[jp_class_scheme] ASC,\n" +
                            "\t[jp_class_symbol] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = ON) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls223_appln_docus](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[docus_class_symbol] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[docus_class_symbol] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = ON) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls224_appln_cpc](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[cpc_class_symbol] [varchar](19) NOT NULL DEFAULT (''),\n" +
                            "\t[cpc_scheme] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[cpc_version] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[cpc_value] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[cpc_position] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[cpc_gener_auth] [char](2) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[cpc_class_symbol] ASC,\n" +
                            "\t[cpc_scheme] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls226_person_orig](\n" +
                            "\t[person_orig_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[person_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[source] [char](5) NOT NULL DEFAULT (''),\n" +
                            "\t[source_version] [varchar](10) NOT NULL DEFAULT (''),\n" +
                            "\t[name_freeform] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[last_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[first_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[middle_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[address_freeform] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[street] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[city] [nvarchar](200) NOT NULL DEFAULT (''),\n" +
                            "\t[zip_code] [nvarchar](30) NOT NULL DEFAULT (''),\n" +
                            "\t[state] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[person_ctry_code] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[residence_ctry_code] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[role] [varchar](2) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[person_orig_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls227_pers_publn](\n" +
                            "\t[person_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[pat_publn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[applt_seq_nr] [smallint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[invt_seq_nr] [smallint] NOT NULL DEFAULT ('0')\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[person_id] ASC,\n" +
                            "\t[pat_publn_id] ASC,\n" +
                            "\t[applt_seq_nr] ASC,\n" +
                            "\t[invt_seq_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls228_docdb_fam_citn](\n" +
                            "\t[docdb_family_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[cited_docdb_family_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[docdb_family_id] ASC,\n" +
                            "\t[cited_docdb_family_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = ON) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls229_appln_nace2](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[nace2_code] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[weight] [real] NOT NULL DEFAULT (1),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[nace2_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls230_appln_techn_field](\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[techn_field_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[weight] [real] NOT NULL DEFAULT (1),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[appln_id] ASC,\n" +
                            "\t[techn_field_nr] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = ON) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls231_inpadoc_legal_event](\n" +
                            "\t[event_id] [int] NOT NULL DEFAULT '0',\n" +
                            "\t[appln_id] [int] NOT NULL DEFAULT '0',\n" +
                            "\t[event_seq_nr] [smallint] NOT NULL default '0',\n" +
                            "\t[event_type] [char](3) NOT NULL DEFAULT ('\t'),\n" +
                            "\t[event_auth] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[event_code] [varchar](4)  NOT NULL DEFAULT (''),\n" +
                            "\t[event_filing_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[event_publn_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[event_effective_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[event_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[ref_doc_auth] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[ref_doc_nr] [varchar](20) NOT NULL DEFAULT (''),\n" +
                            "\t[ref_doc_kind] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[ref_doc_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[ref_doc_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[party_type] [varchar](3) NOT NULL DEFAULT ('\t'),\n" +
                            "\t[party_seq_nr] [smallint] NOT NULL default '0',\n" +
                            "\t[party_new] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[party_old] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[spc_nr] [varchar](40) NOT NULL DEFAULT (''),\n" +
                            "\t[spc_filing_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[spc_patent_expiry_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[spc_extension_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[spc_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[designated_states] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[extension_states] [varchar](30) NOT NULL DEFAULT (''),\n" +
                            "\t[fee_country] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[fee_payment_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[fee_renewal_year] [smallint] NOT NULL default '9999',\n" +
                            "\t[fee_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[lapse_country] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[lapse_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[lapse_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[reinstate_country] [char](2) NOT NULL DEFAULT ('  '),\n" +
                            "\t[reinstate_date] [date] NOT NULL DEFAULT ('9999-12-31'),\n" +
                            "\t[reinstate_text] [varchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[class_scheme] [varchar](4) NOT NULL DEFAULT (''),\n" +
                            "\t[class_symbol] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[event_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls801_country](\n" +
                            "\t[ctry_code] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[iso_alpha3] [char](3) NOT NULL DEFAULT (''),\n" +
                            "\t[st3_name] [varchar](100) NOT NULL DEFAULT (''),\n" +
                            "\t[state_indicator] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[continent] [varchar](25) NOT NULL DEFAULT (''),\n" +
                            "\t[eu_member] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[epo_member] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[oecd_member] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[discontinued] [char](1) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[ctry_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls803_legal_event_code](\n" +
                            "\t[event_auth] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[event_code] [varchar](4) NOT NULL DEFAULT (''),\n" +
                            "\t[event_impact] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[event_descr] [varchar](250) NOT NULL DEFAULT (''),\n" +
                            "\t[event_descr_orig] [varchar](250) NOT NULL DEFAULT (''),\n" +
                            "\t[event_category_code] [char](1) NOT NULL DEFAULT (''),\n" +
                            "\t[event_category_title] [varchar](100) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[event_auth] ASC,\n" +
                            "\t[event_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls901_techn_field_ipc](\n" +
                            "\t[ipc_maingroup_symbol] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[techn_field_nr] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[techn_sector] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[techn_field] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[ipc_maingroup_symbol] ASC\t\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls902_ipc_nace2](\n" +
                            "\t[ipc] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[not_with_ipc] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[unless_with_ipc] [varchar](8) NOT NULL DEFAULT (''),\n" +
                            "\t[nace2_code] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[nace2_weight] [tinyint] NOT NULL DEFAULT (1),\n" +
                            "\t[nace2_descr] [varchar](150) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[ipc] ASC,\n" +
                            "\t[not_with_ipc] ASC,\n" +
                            "\t[unless_with_ipc] ASC,\n" +
                            "\t[nace2_code] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls904_nuts](\n" +
                            "\t[nuts] [varchar](5) NOT NULL DEFAULT (''),\n" +
                            "\t[nuts_level] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[nuts_label] [nvarchar](250) NOT NULL DEFAULT (''),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[nuts] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]\n" +
                            "CREATE TABLE [dbo].[tls906_person](\n" +
                            "\t[person_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[person_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[person_address] [nvarchar](1000) NOT NULL DEFAULT (''),\n" +
                            "\t[person_ctry_code] [char](2) NOT NULL DEFAULT (''),\n" +
                            "\t[nuts] [varchar](5) NOT NULL DEFAULT '',\n" +
                            "\t[nuts_level] [tinyint] NOT NULL DEFAULT ('9'),\n" +
                            "\t[doc_std_name_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[doc_std_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[psn_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[psn_level] [tinyint] NOT NULL DEFAULT ('0'),\n" +
                            "\t[psn_sector] [varchar](50) NOT NULL DEFAULT (''),\n" +
                            "\t[han_id] [int] NOT NULL DEFAULT ('0'),\n" +
                            "\t[han_name] [nvarchar](500) NOT NULL DEFAULT (''),\n" +
                            "\t[han_harmonized] [int] NOT NULL DEFAULT ('0'),\n" +
                            "PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            "\t[person_id] ASC\n" +
                            ")WITH (IGNORE_DUP_KEY = OFF) ON [PRIMARY]\n" +
                            ") ON [PRIMARY]";
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(sql);
                        System.out.println("Done.");
                    }
//                    Insert Data
                    System.out.println("Inserting Data.");
                    LoadCSV csvLoader = new LoadCSV(connection);
                    String sqlInsert = "INSERT INTO dbo.tls201_appln (appln_id,appln_auth,appln_nr,appln_kind,appln_filing_date,appln_filing_year,appln_nr_epodoc,appln_nr_original,ipr_type,receiving_office,internat_appln_id,int_phase,reg_phase,nat_phase,earliest_filing_date,earliest_filing_year,earliest_filing_id,earliest_publn_date,earliest_publn_year,earliest_pat_publn_id,granted,docdb_family_id,inpadoc_family_id,docdb_family_size,nb_citing_docdb_fam,nb_applicants,nb_inventors) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls201_appln.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("1. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls202_appln_title (appln_id,appln_title_lg,appln_title) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls202_appln_title.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("2. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls204_appln_prior (appln_id,prior_appln_id,prior_appln_seq_nr) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls204_appln_prior.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("3. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls205_tech_rel (appln_id,tech_rel_appln_id) \n" +
                            "VALUES (?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls205_tech_rel.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("4. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls206_person (person_id,person_name,person_address,person_ctry_code,nuts,nuts_level,doc_std_name_id,doc_std_name,psn_id,psn_name,psn_level,psn_sector,han_id,han_name,han_harmonized) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls206_person.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("5. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls207_pers_appln (person_id,appln_id,applt_seq_nr,invt_seq_nr) \n" +
                            "VALUES (?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls207_pers_appln.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("6. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls209_appln_ipc (appln_id,ipc_class_symbol,ipc_class_level,ipc_version,ipc_value,ipc_position,ipc_gener_auth) \n" +
                            "VALUES (?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls209_appln_ipc.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("7. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls210_appln_n_cls (appln_id,nat_class_symbol) \n" +
                            "VALUES (?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls210_appln_n_cls.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("8. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls211_pat_publn (pat_publn_id,publn_auth,publn_nr,publn_nr_original,publn_kind,appln_id,publn_date,publn_lg,publn_first_grant,publn_claims) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls211_pat_publn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("9. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls212_citation (pat_publn_id,citn_replenished,citn_id,citn_origin,cited_pat_publn_id,cited_appln_id,pat_citn_seq_nr,cited_npl_publn_id,npl_citn_seq_nr,citn_gener_auth) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls212_citation.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("10. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls214_npl_publn (npl_publn_id,npl_type,npl_biblio,npl_author,npl_title1,npl_title2,npl_editor,npl_volume,npl_issue,npl_publn_date,npl_publn_end_date,npl_publisher,npl_page_first,npl_page_last,npl_abstract_nr,npl_doi,npl_isbn,npl_issn,online_availability,online_classification,online_search_date) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls214_npl_publn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("11. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls215_citn_categ (pat_publn_id,citn_replenished,citn_id,citn_categ) \n" +
                            "VALUES (?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls215_citn_categ.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("12. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls216_appln_contn (appln_id,parent_appln_id,contn_type) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls216_appln_contn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("13. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls222_appln_jp_class (appln_id,jp_class_scheme,jp_class_symbol) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls222_appln_jp_class.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("14. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls223_appln_docus (appln_id,docus_class_symbol) \n" +
                            "VALUES (?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls223_appln_docus.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("15. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls224_appln_cpc (appln_id,cpc_class_symbol,cpc_scheme,cpc_version,cpc_value,cpc_position,cpc_gener_auth) \n" +
                            "VALUES (?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls224_appln_cpc.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("16. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls226_person_orig (person_orig_id,person_id,source,source_version,name_freeform,last_name,first_name,middle_name,address_freeform,street,city,zip_code,state,person_ctry_code,residence_ctry_code,role) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls226_person_orig.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("17. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls227_pers_publn (person_id,pat_publn_id,applt_seq_nr,invt_seq_nr) \n" +
                            "VALUES (?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls227_pers_publn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("18. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls228_docdb_fam_citn (docdb_family_id,cited_docdb_family_id) \n" +
                            "VALUES (?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls228_docdb_fam_citn.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("19. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls229_appln_nace2 (appln_id,nace2_code,weight) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls229_appln_nace2.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("20. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls230_appln_techn_field (appln_id,techn_field_nr,weight) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls230_appln_techn_field.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("21. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls231_inpadoc_legal_event (event_id,appln_id,event_seq_nr,event_type,event_auth,event_code,event_filing_date,event_publn_date,event_effective_date,event_text,ref_doc_auth,ref_doc_nr,ref_doc_kind,ref_doc_date,ref_doc_text,party_type,party_seq_nr,party_new,party_old,spc_nr,spc_filing_date,spc_patent_expiry_date,spc_extension_date,spc_text,designated_states,extension_states,fee_country,fee_payment_date,fee_renewal_year,fee_text,lapse_country,lapse_date,lapse_text,reinstate_country,reinstate_date,reinstate_text,class_scheme,class_symbol) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls231_inpadoc_legal_event.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("22. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls801_country (ctry_code,iso_alpha3,st3_name,state_indicator,continent,eu_member,epo_member,oecd_member,discontinued) \n" +
                            "VALUES (?,?,?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls801_country.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("23. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls803_legal_event_code (event_auth,event_code,event_impact,event_descr,event_descr_orig,event_category_code,event_category_title) \n" +
                            "VALUES (?,?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls803_legal_event_code.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("24. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls901_techn_field_ipc (ipc_maingroup_symbol,techn_field_nr,techn_sector,techn_field) \n" +
                            "VALUES (?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls901_techn_field_ipc.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("25. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls902_ipc_nace2 (ipc,not_with_ipc,unless_with_ipc,nace2_code,nace2_weight,nace2_descr) \n" +
                            "VALUES (?,?,?,?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls902_ipc_nace2.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("26. Table Done");

                    sqlInsert = "INSERT INTO dbo.tls904_nuts (nuts,nuts_level,nuts_label) \n" +
                            "VALUES (?,?,?)";
                    try {
                        csvLoader.insertData("C:\\Users\\Daveb\\IdeaProjects\\prace\\csvData\\tls904_nuts.csv", sqlInsert);
                    } catch (Exception e){
                        System.out.println();
                        e.printStackTrace();
                    }
                    System.out.println("27. Table Done");

                    System.out.println("Data import completed.");

//                    sql =   "USE register2018b\n" +
//                            "SELECT TOP 5\n" +
//                            "\t   appln.id 'document.application_id' \n" +
//                            "\t  ,appln.appln_id 'application.appln_id'\n" +
//                            "      ,appln.appln_auth 'application.appln_auth'\n" +
//                            "      ,appln.appln_nr 'application.appln_nr'\n" +
//                            "      ,appln.appln_filing_date 'application.appln_filing_date'\n" +
//                            "      ,appln.filing_lg 'application.filing_lg'\n" +
//                            "      ,appln.status 'application.status'\n" +
//                            "      ,appln.internat_appln_id 'application.internat_appln_id'\n" +
//                            "      ,appln.internat_appln_nr 'application.internat_appln_nr'\n" +
//                            "\t  ,status.status_text 'application.status_text'\n" +
//                            "\t  ,(SELECT  \n" +
//                            "\t\t   party.name 'party.name'\n" +
//                            "\t\t  ,party.set_seq_nr 'party.set_seq_nr'\n" +
//                            "\t\t  ,party.is_latest 'party.is_latest'\n" +
//                            "\t\t  ,party.change_date 'party.change_date'\n" +
//                            "\t\t  ,party.bulletin_year 'party.bulletin_year'\n" +
//                            "\t\t  ,party.bulletin_nr 'party.bulletin_nr'\n" +
//                            "\t\t  ,party.type 'party.type'\n" +
//                            "\t\t  ,party.wishes_to_be_published 'party.wishes_to_be_published'\n" +
//                            "\t\t  ,party.seq_nr 'party.seq_nr'\n" +
//                            "\t\t  ,party.designation 'party.designation'\n" +
//                            "\t\t  ,party.customer_id 'party.customer_id'\n" +
//                            "\t\t  ,party.address_1 'party.address_1'\n" +
//                            "\t\t  ,party.address_2 'party.address_2'\n" +
//                            "\t\t  ,party.address_3 'party.address_3'\n" +
//                            "\t\t  ,party.address_4 'party.address_4'\n" +
//                            "\t\t  ,party.address_5 'party.address_5'\n" +
//                            "\t\t  ,party.country 'party.country'\n" +
//                            "\t\t  ,(SELECT \n" +
//                            "\t  \t\t\tapplicant_states.type 'applicant.type'\n" +
//                            "\t\t\t\t,applicant_states.set_seq_nr 'applicant.set_seq_nr'\n" +
//                            "\t\t\t\t,applicant_states.bulletin_year 'applicant.bulletin_year'\n" +
//                            "\t\t\t\t,applicant_states.bulletin_nr 'applicant.bulletin_nr'\n" +
//                            "\t\t\t\t,applicant_states.seq_nr 'applicant.seq_nr'\n" +
//                            "\t\t\t\t,applicant_states.country 'applicant.country'\n" +
//                            "\t\t\t\tFROM reg108_applicant_states applicant_states\n" +
//                            "\t\t\t\tWHERE applicant_states.id = party.id\n" +
//                            "\t\t\t\tFOR JSON PATH) applicant_states\n" +
//                            "\t\t  FROM reg107_parties party\n" +
//                            "\t\t  WHERE party.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) authors\n" +
//                            "\t  ,(SELECT \n" +
//                            "\t\t   title.title 'title.title'\n" +
//                            "\t\t  ,title.change_date 'title.change_date'\n" +
//                            "\t\t  ,title.bulletin_year 'title.bulletin_year'\n" +
//                            "\t\t  ,title.bulletin_nr 'title.bulletin_nr'\n" +
//                            "\t\t  ,title.title_lg 'title.title_lg'\n" +
//                            "\t\t  FROM reg110_title title\n" +
//                            "\t\t  WHERE title.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) title\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t   pat_publn.bulletin_year 'abstract.bulletin_year'\n" +
//                            "\t\t  ,pat_publn.bulletin_nr 'abstract.bulletin_nr'\n" +
//                            "\t\t  ,pat_publn.publn_auth 'abstract.publn_auth'\n" +
//                            "\t\t  ,pat_publn.publn_nr 'abstract.publn_nr'\n" +
//                            "\t\t  ,pat_publn.publn_kind 'abstract.publn_kind'\n" +
//                            "\t\t  ,pat_publn.publn_date 'abstract.publn_date'\n" +
//                            "\t\t  ,pat_publn.publn_lg 'abstract.publn_lg'\n" +
//                            "\t\t  FROM reg102_pat_publn pat_publn\n" +
//                            "\t\t  WHERE pat_publn.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) abstract\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t   ipc.ipc_text 'ipc.ipc_text'\n" +
//                            "\t\t  ,ipc.change_date 'ipc.change_date'\n" +
//                            "\t\t  ,ipc.bulletin_year 'ipc.bulletin_year'\n" +
//                            "\t\t  ,ipc.bulletin_nr 'ipc.bulletin_nr'\n" +
//                            "\t\t  FROM reg103_ipc ipc\n" +
//                            "\t\t  WHERE ipc.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) ipc\n" +
//                            "\t  ,(SELECT \n" +
//                            "\t\t   prior.change_date 'prior.change_date'\n" +
//                            "\t\t  ,prior.bulletin_year 'prior.bulletin_year'\n" +
//                            "\t\t  ,prior.bulletin_nr 'prior.bulletin_nr'\n" +
//                            "\t\t  ,prior.prior_seq_nr 'prior.prior_seq_nr'\n" +
//                            "\t\t  ,prior.prior_kind 'prior.prior_kind'\n" +
//                            "\t\t  ,prior.prior_auth 'prior.prior_auth'\n" +
//                            "\t\t  ,prior.prior_nr 'prior.prior_nr'\n" +
//                            "\t\t  ,prior.prior_date 'prior.prior_date'\n" +
//                            "\t\t  FROM reg106_prior prior\n" +
//                            "\t\t  WHERE prior.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) prior\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t   designated_states.state_type 'designated_states.state_type'\n" +
//                            "\t\t  ,designated_states.change_date 'designated_states.change_date'\n" +
//                            "\t\t  ,designated_states.bulletin_year 'designated_states.bulletin_year'\n" +
//                            "\t\t  ,designated_states.bulletin_nr 'designated_states.bulletin_nr'\n" +
//                            "\t\t  ,designated_states.designated_states 'designated_states.designated_states'\n" +
//                            "\t\t  FROM reg109_designated_states designated_states\n" +
//                            "\t\t  WHERE designated_states.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) designated_states\n" +
//                            "\t\t  \t  ,(SELECT\n" +
//                            "\t\t   licensee.change_date 'licensee.change_date'\n" +
//                            "\t\t  ,licensee.bulletin_year 'licensee.bulletin_year'\n" +
//                            "\t\t  ,licensee.bulletin_nr 'licensee.bulletin_nr'\n" +
//                            "\t\t  ,licensee.licensee_seq_nr 'licensee.licensee_seq_nr'\n" +
//                            "\t\t  ,licensee.type_license 'licensee.type_license'\n" +
//                            "\t\t  ,licensee.designation 'licensee.designation'\n" +
//                            "\t\t  ,licensee.valid_date 'licensee.valid_date'\n" +
//                            "\t\t  ,licensee.customer_id 'licensee.customer_id'\n" +
//                            "\t\t  ,licensee.name 'licensee.name'\n" +
//                            "\t\t  ,licensee.address_1 'licensee.address_1'\n" +
//                            "\t\t  ,licensee.address_2 'licensee.address_2'\n" +
//                            "\t\t  ,licensee.address_3 'licensee.address_3'\n" +
//                            "\t\t  ,licensee.address_4 'licensee.address_4'\n" +
//                            "\t\t  ,licensee.address_5 'licensee.address_5'\n" +
//                            "\t\t  ,licensee.country 'licensee.country'\n" +
//                            "\t\t  ,(SELECT\n" +
//                            "\t\t\t\t licensee_states.bulletin_year 'licensee_states.bulletin_year'\n" +
//                            "\t\t\t    ,licensee_states.bulletin_nr 'licensee_states.bulletin_nr'\n" +
//                            "\t\t\t    ,licensee_states.licensee_seq_nr 'licensee_states.licensee_seq_nr'\n" +
//                            "\t\t\t    ,licensee_states.licensee_country 'licensee_states.licensee_country'\n" +
//                            "\t\t\t\tFROM reg112_licensee_states licensee_states\n" +
//                            "\t\t\t\tWHERE licensee.id = licensee_states.id\n" +
//                            "\t\t\t\tFOR JSON PATH) licensee_states\n" +
//                            "\t\t  FROM reg111_licensee licensee\n" +
//                            "\t\t  WHERE licensee.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) licensee\n" +
//                            "\t,(SELECT\n" +
//                            "\t\tterms_of_grant.change_date 'terms_of_grant.change_date'\n" +
//                            "\t\t,terms_of_grant.bulletin_year 'terms_of_grant.bulletin_year'\n" +
//                            "        ,terms_of_grant.bulletin_nr 'terms_of_grant.bulletin_nr'\n" +
//                            "\t\t,terms_of_grant.lapsed_country 'terms_of_grant.lapsed_country'\n" +
//                            "\t    ,terms_of_grant.lapsed_date 'terms_of_grant.lapsed_date'\n" +
//                            "\t\tFROM reg113_terms_of_grant terms_of_grant\n" +
//                            "\t\tWHERE terms_of_grant.id = appln.id\n" +
//                            "\t\tFOR JSON PATH) terms_of_grant\n" +
//                            "\t,(SELECT\n" +
//                            "\t\tdates.bulletin_year 'dates.bulletin_year'\n" +
//                            "\t\t,dates.bulletin_nr 'dates.bulletin_nr'\n" +
//                            "\t\t,dates.date_type 'dates.date_type'\n" +
//                            "\t\t,dates.event_date 'dates.event_date'\n" +
//                            "\t\t,dates.cause_interruption 'dates.cause_interruption'\n" +
//                            "\t\t,dates.converted_to_country 'dates.converted_to_country'\n" +
//                            "\t\tFROM reg114_dates dates\n" +
//                            "\t\tWHERE dates.id = appln.id\n" +
//                            "\t\tFOR JSON PATH) dates\n" +
//                            "\t,(SELECT\n" +
//                            "\t\trelation.relation_type 'relation.relation_type'\n" +
//                            "      ,relation.child_id 'relation.child_id'\n" +
//                            "\t  FROM reg117_relation relation\n" +
//                            "\t  WHERE relation.id = appln.id\n" +
//                            "\tFOR JSON PATH) relation\n" +
//                            "    ,(SELECT\n" +
//                            "\t   prev_filed_appln.id 'prev_filled_appln.appln_id'\n" +
//                            "\t  ,prev_filed_appln.bulletin_year 'prev_filled_appln.appln_bulletin_year'\n" +
//                            "      ,prev_filed_appln.bulletin_nr 'prev_filled_appln.appln_bulletin_nr'\n" +
//                            "      ,prev_filed_appln.appln_auth 'prev_filled_appln.appln_appln_auth'\n" +
//                            "      ,prev_filed_appln.appln_nr 'prev_filled_appln.appln_appln_nr'\n" +
//                            "      ,prev_filed_appln.appln_date 'prev_filled_appln.appln_appln_date'\n" +
//                            "\t  FROM reg118_prev_filed_appln prev_filed_appln\n" +
//                            "\t  WHERE prev_filed_appln.id = appln.id\n" +
//                            "\t  FOR JSON PATH) prev_filled_appln\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t appeal.appeal_date 'appeal.appeal_date'\n" +
//                            "\t\t,appeal.appeal_nr 'appeal.appeal_nr'\n" +
//                            "\t\t,appeal.phase 'appeal.phase'\n" +
//                            "\t\t,appeal.date_state_grounds_filed 'appeal.date_state_grounds_filed'\n" +
//                            "\t\t,appeal.date_interloc_revision 'appeal.date_interloc_revision'\n" +
//                            "\t\t,appeal.result 'appeal.result'\n" +
//                            "\t\t,appeal.result_date 'appeal.result_date'\n" +
//                            "\t  FROM reg125_appeal appeal\n" +
//                            "\t  WHERE appeal.id = appln.id\n" +
//                            "\t  FOR JSON PATH) appeal\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t   petition_rvw.review_nr 'petition.review_nr'\n" +
//                            "\t\t  ,petition_rvw.appeal_nr 'petition.appeal_nr'\n" +
//                            "\t\t  ,petition_rvw.review_date 'petition.review_date'\n" +
//                            "\t\t  ,petition_rvw.petitioner_code 'petition.petitioner_code'\n" +
//                            "\t\t  ,petition_rvw.review_decision_date 'petition.review_decision_date'\n" +
//                            "\t\t  ,petition_rvw.review_kind 'petition.review_kind'\n" +
//                            "\t\tFROM reg127_petition_rvw petition_rvw\n" +
//                            "\t\tWHERE petition_rvw.id = appln.id\n" +
//                            "\t\tFOR JSON PATH) petition_review\n" +
//                            "\t  ,(SELECT\n" +
//                            "\t\t   limitation.limit_seq_nr 'limitation.limit_seq_nr'\n" +
//                            "\t\t  ,limitation.limitation_filing_date 'limitation.limitation_filing_date'\n" +
//                            "\t\t  ,limitation.limitation_filing_decision 'limitation.limitation_filing_decision'\n" +
//                            "\t\t  ,limitation.date_dispatch_allowance 'limitation.date_dispatch_allowance'\n" +
//                            "\t\t  ,limitation.date_payment_allowance 'limitation.date_payment_allowance'\n" +
//                            "\t\t  ,limitation.date_dispatch_rejection 'limitation.date_dispatch_rejection'\n" +
//                            "\t\t  ,limitation.date_legal_effect_rejection 'limitation.date_legal_effect_rejection'\n" +
//                            "\t\tFROM reg128_limitation limitation\n" +
//                            "\t\tWHERE limitation.id = appln.id\n" +
//                            "\t\tFOR JSON PATH) limitation\n" +
//                            "\t\t,(SELECT\n" +
//                            "\t\t   opponent.change_date 'opponent.change_date'\n" +
//                            "\t\t  ,opponent.bulletin_year 'opponent.bulletin_year'\n" +
//                            "\t\t  ,opponent.bulletin_nr 'opponent.bulletin_nr'\n" +
//                            "\t\t  ,opponent.is_latest 'opponent.is_latest'\n" +
//                            "\t\t  ,opponent.oppt_nr 'opponent.oppt_nr'\n" +
//                            "\t\t  ,opponent.customer_id 'opponent.customer_id'\n" +
//                            "\t\t  ,opponent.oppt_name 'opponent.oppt_name'\n" +
//                            "\t\t  ,opponent.oppt_address_1 'opponent.oppt_address_1'\n" +
//                            "\t\t  ,opponent.oppt_address_2 'opponent.oppt_address_2' \n" +
//                            "\t\t  ,opponent.oppt_address_3 'opponent.oppt_address_3' \n" +
//                            "\t\t  ,opponent.oppt_address_4 'opponent.oppt_address_4' \n" +
//                            "\t\t  ,opponent.oppt_address_5 'opponent.oppt_address_5' \n" +
//                            "\t\t  ,opponent.oppt_country 'opponent.oppt_country' \n" +
//                            "\t\t  ,opponent.date_opp_filed 'opponent.date_opp_filed'\n" +
//                            "\t\t  ,opponent.oppt_status 'opponent.oppt_status'\n" +
//                            "\t\t  ,opponent.oppt_status_date 'opponent.oppt_status_date'\n" +
//                            "\t\t  ,opponent.agent_name 'opponent.agent_name'\n" +
//                            "\t\t  ,opponent.agent_address_1 'opponent.agent_address_1'\n" +
//                            "\t\t  ,opponent.agent_address_2 'opponent.agent_address_2'  \n" +
//                            "\t\t  ,opponent.agent_address_3 'opponent.agent_address_3'\n" +
//                            "\t\t  ,opponent.agent_address_4 'opponent.agent_address_4' \n" +
//                            "\t\t  ,opponent.agent_address_5 'opponent.agent_address_5' \n" +
//                            "\t\t  ,opponent.agent_country 'opponent.agent_country' \n" +
//                            "\t\t  FROM reg130_opponent opponent\n" +
//                            "\t\t  WHERE opponent.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) opponent\n" +
//                            "\t\t  ,(SELECT\n" +
//                            "\t\t\t  text.change_date 'text.change_date'\n" +
//                            "\t\t\t ,text.bulletin_year 'text.bulletin_year'\n" +
//                            "\t\t\t ,text.bulletin_nr 'text.bulletin_nr'\n" +
//                            "\t\t\t ,text.text_lg 'text.text_lg'\n" +
//                            "\t\t\t ,text.miscellaneous_text 'text.miscellaneous_text'\n" +
//                            "\t\t  FROM reg135_text text\n" +
//                            "\t\t  WHERE text.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) text\n" +
//                            "\t\t  ,(SELECT\n" +
//                            "\t\t\t   search_report.bulletin_year 'search_reportbulletin_year'\n" +
//                            "\t\t\t  ,search_report.bulletin_nr 'search_reportbulletin_nr'\n" +
//                            "\t\t\t  ,search_report.office 'search_reportoffice'\n" +
//                            "\t\t\t  ,search_report.search_type 'search_reportsearch_type'\n" +
//                            "\t\t\t  ,search_report.mailed_date 'search_reportmailed_date'\n" +
//                            "\t\t\t  ,search_report.publn_auth 'search_reportpubln_auth'\n" +
//                            "\t\t\t  ,search_report.publn_nr 'search_reportpubln_nr'\n" +
//                            "\t\t\t  ,search_report.publn_kind 'search_reportpubln_kind'\n" +
//                            "\t\t\t  ,search_report.publn_date 'search_reportpubln_date'\n" +
//                            "\t\t\t  ,search_report.publn_lg 'search_reportpubln_lg'\n" +
//                            "\t\t  FROM reg136_search_report search_report\n" +
//                            "\t\t  WHERE search_report.id = appln.id\n" +
//                            "\t\t  FOR JSON PATH) search_report\n" +
//                            "\t\t  ,(SELECT\n" +
//                            "\t\t\t  proc_step.step_id 'proc_step.step_id'\n" +
//                            "\t\t\t  ,proc_step.step_phase 'proc_step.step_phase'\n" +
//                            "\t\t\t  ,proc_step.step_code 'proc_step.step_code'\n" +
//                            "\t\t\t  ,proc_step.step_result 'proc_step.step_result'\n" +
//                            "\t\t\t  ,proc_step.step_result_type 'proc_step.step_result_type' \n" +
//                            "\t\t\t  ,proc_step.step_country 'proc_step.step_country'\n" +
//                            "\t\t\t  ,proc_step.time_limit 'proc_step.time_limit' \n" +
//                            "\t\t\t  ,proc_step.time_limit_unit 'proc_step.time_limit_unit'\n" +
//                            "\t\t\t  ,proc_step.bulletin_year 'proc_step.bulletin_year'\n" +
//                            "\t\t\t  ,proc_step.bulletin_nr 'proc_step.bulletin_nr'\n" +
//                            "\t\t\t  ,(SELECT  \n" +
//                            "\t\t\t\t\tproc_step_text.step_id 'proc_step_text.text_step_id'\n" +
//                            "\t\t\t\t   ,proc_step_text.step_text 'proc_step_text.text_step_text'\n" +
//                            "\t\t\t\t   ,proc_step_text.step_text_type 'proc_step_text.text_step_text_type'\n" +
//                            "\t\t\t\t  FROM reg202_proc_step_text proc_step_text\n" +
//                            "\t\t\t\t  WHERE proc_step_text.id = proc_step.id\n" +
//                            "\t\t\t\t  FOR JSON PATH) proc_step_text\n" +
//                            "\t\t\t   ,(SELECT\n" +
//                            "\t\t\t\t\tproc_step_date.step_id 'proc_step_date.step_id'\n" +
//                            "\t\t\t\t   ,proc_step_date.step_date 'proc_step_date.step_date'\n" +
//                            "\t\t\t\t   ,proc_step_date.step_date_type 'proc_step_date.step_date_type'\n" +
//                            "\t\t\t\t   FROM reg203_proc_step_date proc_step_date\n" +
//                            "\t\t\t\t   WHERE proc_step_date.id = proc_step.id\n" +
//                            "\t\t\t\t  FOR JSON PATH) proc_step_date\n" +
//                            "\t\t\tFROM reg201_proc_step proc_step\n" +
//                            "\t\t\tWHERE proc_step.id = appln.id\n" +
//                            "\t\t\tFOR JSON PATH) proc_step\n" +
//                            "\t\t,(SELECT \n" +
//                            "\t\t\tevent_data.event_date 'event_data.event_date'\n" +
//                            "\t\t\t,event_data.event_code 'event_data.event_code' \n" +
//                            "\t\t\t,event_data.bulletin_year 'event_data.bulletin_year'\n" +
//                            "\t\t\t,event_data.bulletin_nr 'event_data.bulletin_nr'\n" +
//                            "\t\t\t,event_data.bulletin_date 'event_data.bulletin_date'\n" +
//                            "\t\t\t,(SELECT\n" +
//                            "\t\t\t\tevent_text.event_text 'event_text.event_text'\n" +
//                            "\t\t\t FROM reg402_event_text event_text\n" +
//                            "\t\t\t WHERE event_data.event_code = event_text.event_code\n" +
//                            "\t\t\t FOR JSON PATH) event_text\n" +
//                            "\t\t\tFROM reg301_event_data event_data\n" +
//                            "\t\t\tWHERE event_data.id = appln.id\n" +
//                            "\t\t\tFOR JSON PATH) event_data\t\t\t\n" +
//                            "  FROM reg101_appln as appln\n" +
//                            "  LEFT JOIN reg403_appln_status status on appln.status = status.status\n" +
//                            "  FOR JSON PATH, ROOT('PATSTAT')\n";

//                    try (Statement statement = connection.createStatement()){
//                        System.out.println("Starting String export.");
//                        ResultSet rs = statement.executeQuery(sql);
//                        PrintWriter writer = new PrintWriter("./convertedData/test.json", "UTF-8");
//                        String editedJson = "";
//                        String jsonText = "";
//
//                        Instant start = Instant.now();
//
//                        while(rs.next()){
//                            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
//                                jsonText = new StringBuilder().append(jsonText).append(rs.getString(i)).toString();
//                            }
//                        }
//
//                        GsonBuilder builder = new GsonBuilder();
//                        builder.disableHtmlEscaping();
//                        Gson gson = builder.setPrettyPrinting().create();
//                        JsonParser jParser = new JsonParser();
//                        JsonElement jElement = jParser.parse(jsonText);
//                        editedJson = gson.toJson(jElement);
//
//                        writer.print(editedJson);
//                        writer.close();
//
//
//                        Instant finish = Instant.now();
//                        long timeElapsed = Duration.between(start, finish).toMillis();
//
//                        System.out.println(timeElapsed);
//                    } catch (SQLException e){
//                        e.printStackTrace();
//                    }
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