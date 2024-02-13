USE [master]
GO
/****** Object:  Database [Poptopia]    Script Date: 13-02-2024 12:18:38 ******/
CREATE DATABASE [Poptopia]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Poptopia', FILENAME = N'D:\rdsdbdata\DATA\Poptopia.mdf' , SIZE = 4480KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'Poptopia_log', FILENAME = N'D:\rdsdbdata\DATA\Poptopia_log.ldf' , SIZE = 4096KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [Poptopia] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Poptopia].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Poptopia] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Poptopia] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Poptopia] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Poptopia] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Poptopia] SET ARITHABORT OFF 
GO
ALTER DATABASE [Poptopia] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Poptopia] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Poptopia] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Poptopia] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Poptopia] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Poptopia] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Poptopia] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Poptopia] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Poptopia] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Poptopia] SET  ENABLE_BROKER 
GO
ALTER DATABASE [Poptopia] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Poptopia] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Poptopia] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Poptopia] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Poptopia] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Poptopia] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Poptopia] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Poptopia] SET RECOVERY FULL 
GO
ALTER DATABASE [Poptopia] SET  MULTI_USER 
GO
ALTER DATABASE [Poptopia] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Poptopia] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Poptopia] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Poptopia] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [Poptopia] SET DELAYED_DURABILITY = DISABLED 
GO
USE [Poptopia]
GO
/****** Object:  User [Poptopia_readonly]    Script Date: 13-02-2024 12:18:40 ******/
CREATE USER [Poptopia_readonly] FOR LOGIN [Poptopia_readonly] WITH DEFAULT_SCHEMA=[dbo]
GO
/****** Object:  User [Poptopia]    Script Date: 13-02-2024 12:18:40 ******/
CREATE USER [Poptopia] FOR LOGIN [Poptopia] WITH DEFAULT_SCHEMA=[dbo]
GO
/****** Object:  User [admin]    Script Date: 13-02-2024 12:18:40 ******/
CREATE USER [admin] FOR LOGIN [admin] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_datareader] ADD MEMBER [Poptopia_readonly]
GO
ALTER ROLE [db_owner] ADD MEMBER [Poptopia]
GO
ALTER ROLE [db_owner] ADD MEMBER [admin]
GO
/****** Object:  Table [dbo].[answers]    Script Date: 13-02-2024 12:18:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[answers](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[answer] [nvarchar](2000) NOT NULL,
	[promotion_entry_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[api_users_table]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[api_users_table](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[password] [varchar](200) NOT NULL,
	[role] [varchar](30) NOT NULL,
	[username] [varchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_extayw5079uw341mjaae4909j] UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[mini_game]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[mini_game](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [datetime2](7) NOT NULL,
	[game_id] [varchar](100) NULL,
	[profile_id] [varchar](100) NULL,
	[state] [varbinary](255) NULL,
	[updated_date] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_jn28uejvacxacrx37w0h14rq4] UNIQUE NONCLUSTERED 
(
	[game_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promo_prize_config]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promo_prize_config](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[inventory] [int] NOT NULL,
	[is_active] [bit] NOT NULL,
	[max_win] [int] NOT NULL,
	[prize_code] [varchar](255) NOT NULL,
	[prize_name] [varchar](255) NOT NULL,
	[win_probability] [int] NOT NULL,
	[promotion_id] [int] NOT NULL,
	[attribute] [varchar](255) NULL,
	[reference] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK87sl70n5xc723do2uya81s6s8] UNIQUE NONCLUSTERED 
(
	[prize_code] ASC,
	[prize_name] ASC,
	[promotion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_cluster]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_cluster](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[cluster_name] [varchar](150) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_code_entry]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_code_entry](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [varchar](150) NOT NULL,
	[promotion_code_id] [int] NULL,
	[promotion_entry_id] [int] NULL,
	[profile_id] [int] NULL,
	[promotion_id] [int] NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_date_time] [datetime2](7) NULL,
	[reward_used_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_code_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_code_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[code] [varchar](150) NOT NULL,
	[status] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_g48yidyy6euq1vg3km5lqejjo] UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_code_reference]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_code_reference](
	[code_id] [int] NOT NULL,
	[promotion_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[code_id] ASC,
	[promotion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_entry]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_entry](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [date] NOT NULL,
	[modified_date] [date] NOT NULL,
	[answer] [nvarchar](max) NULL,
	[country] [varchar](100) NOT NULL,
	[profile_id] [int] NOT NULL,
	[promotion_id] [int] NULL,
	[retailer_id] [int] NULL,
	[status_id] [int] NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_date_time] [datetime2](7) NULL,
	[attr1_code] [varchar](100) NULL,
	[attr2_code] [varchar](100) NULL,
	[attr1_value] [varchar](300) NULL,
	[attr2_value] [varchar](225) NULL,
	[local_created_date_time] [datetime2](7) NULL,
	[local_time_zone] [varchar](100) NULL,
	[Receipt_Status] [nvarchar](50) NULL,
	[donate_prize] [varchar](255) NULL,
	[invalid_short_text] [varchar](255) NULL,
	[prize_status] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_entry_errorlog]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_entry_errorlog](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date_time] [datetime2](7) NULL,
	[error_code] [int] NULL,
	[error_msg] [varchar](255) NULL,
	[promotion_id] [int] NULL,
	[promotion_entry_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [date] NOT NULL,
	[modified_date] [date] NOT NULL,
	[end_date] [date] NOT NULL,
	[epsilon_sweepstakes_id] [int] NOT NULL,
	[max_redeem_limit] [int] NOT NULL,
	[module_key] [nvarchar](100) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[start_date] [date] NOT NULL,
	[region_id] [int] NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_date_time] [datetime2](7) NULL,
	[local_time_zone] [varchar](100) NULL,
	[attr1_code] [varchar](100) NULL,
	[attr1_value] [varchar](100) NULL,
	[promotion_cluster_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_dpjf8h8qetso0jwac854ft6vr] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[module_key] ASC,
	[epsilon_sweepstakes_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[promotion_user]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[promotion_user](
	[profile_id] [int] NOT NULL,
	[attribute1] [int] NULL,
	[attribute2] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[profile_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[receipt_header]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[receipt_header](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[post_code] [varchar](255) NULL,
	[receipt_trans] [varchar](255) NULL,
	[retailer] [varchar](255) NULL,
	[total_price] [varchar](255) NULL,
	[receipt_id] [int] NULL,
	[city] [varchar](255) NULL,
	[phone] [varchar](255) NULL,
	[state] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[receipt_images]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[receipt_images](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[client_eventid] [varchar](100) NULL,
	[image_status] [varchar](50) NULL,
	[processing_status] [varchar](100) NULL,
	[processing_time] [varchar](100) NULL,
	[processing_type_id] [varchar](100) NULL,
	[s3_image_url] [nvarchar](3000) NULL,
	[snipp_eventid] [nvarchar](300) NULL,
	[validation_type_id] [varchar](100) NULL,
	[promotion_entry_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[receipt_product_details]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[receipt_product_details](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[item_id] [int] NULL,
	[name] [varchar](255) NULL,
	[price] [float] NULL,
	[qty] [int] NULL,
	[receipt_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[region_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[region_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[country] [varchar](255) NOT NULL,
	[locale] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[retailer_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[retailer_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_kplbdahkasn2qx401pm4kfyub] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[retailer_master_promotions]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[retailer_master_promotions](
	[retailer_id] [int] NOT NULL,
	[promotions_id] [int] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reward_consumed]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[reward_consumed](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [date] NOT NULL,
	[modified_date] [date] NOT NULL,
	[reward_code] [varchar](300) NOT NULL,
	[promotion_entry_id] [int] NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_date_time] [datetime2](7) NULL,
	[profile_id] [int] NULL,
	[promotion_id] [int] NULL,
	[reward_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_ckfhtiy5qo86u8p5xy53aq6ao] UNIQUE NONCLUSTERED 
(
	[reward_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reward_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[reward_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[reward_code] [varchar](300) NOT NULL,
	[reward_type] [varchar](100) NOT NULL,
	[status] [varchar](30) NULL,
	[promotion_id] [int] NULL,
	[reward_step] [varchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_fbfc8148q3jhesrdije9nhgbm] UNIQUE NONCLUSTERED 
(
	[reward_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[status_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[status_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[status] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_pswpat6xc5od5mnfkjo6keahn] UNIQUE NONCLUSTERED 
(
	[status] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[token_master]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[token_master](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[answer] [nvarchar](max) NULL,
	[promotion_entry_id] [int] NOT NULL,
	[hash_code] [varchar](300) NOT NULL,
	[user_profile_id] [int] NOT NULL,
	[retailer] [varchar](50) NULL,
	[status] [varchar](50) NOT NULL,
	[expiration_time] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_generated_content]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_generated_content](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [datetime2](7) NOT NULL,
	[end_date] [datetime2](7) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[start_date] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_4owgjddj7a774gyxloe409elx] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_generated_entry]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_generated_entry](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ugc_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[winner_selection_config]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[winner_selection_config](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[end_time] [datetime2](7) NULL,
	[limit] [int] NULL,
	[max_winners] [int] NULL,
	[promotion_date] [date] NULL,
	[start_time] [datetime2](7) NULL,
	[win_probability] [int] NULL,
	[promotion_id] [int] NULL,
	[win_step] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[winner_selection_config_reference]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[winner_selection_config_reference](
	[config_id] [int] NOT NULL,
	[promotion_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[config_id] ASC,
	[promotion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[winners]    Script Date: 13-02-2024 12:18:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[winners](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_date] [date] NOT NULL,
	[modified_date] [date] NOT NULL,
	[promotion_entry_id] [int] NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_date_time] [datetime2](7) NULL,
	[win_frequency] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[promotion_entry_errorlog] ADD  DEFAULT (getdate()) FOR [created_date_time]
GO
ALTER TABLE [dbo].[answers]  WITH CHECK ADD  CONSTRAINT [FKhb3qw47euc03it1o23hr3eh6v] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[answers] CHECK CONSTRAINT [FKhb3qw47euc03it1o23hr3eh6v]
GO
ALTER TABLE [dbo].[promo_prize_config]  WITH CHECK ADD  CONSTRAINT [FKpw0qwo5596g7nui7d4d0vboar] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[promo_prize_config] CHECK CONSTRAINT [FKpw0qwo5596g7nui7d4d0vboar]
GO
ALTER TABLE [dbo].[promotion_code_entry]  WITH CHECK ADD  CONSTRAINT [FK6a8x16edyxgrr5n9kny7tyuih] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[promotion_code_entry] CHECK CONSTRAINT [FK6a8x16edyxgrr5n9kny7tyuih]
GO
ALTER TABLE [dbo].[promotion_code_entry]  WITH CHECK ADD  CONSTRAINT [FKgw4h2car65yq2cwpg1vup4rno] FOREIGN KEY([promotion_code_id])
REFERENCES [dbo].[promotion_code_master] ([id])
GO
ALTER TABLE [dbo].[promotion_code_entry] CHECK CONSTRAINT [FKgw4h2car65yq2cwpg1vup4rno]
GO
ALTER TABLE [dbo].[promotion_code_entry]  WITH CHECK ADD  CONSTRAINT [FKgwcu0qs2u92myhgj41kfmt9l4] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[promotion_code_entry] CHECK CONSTRAINT [FKgwcu0qs2u92myhgj41kfmt9l4]
GO
ALTER TABLE [dbo].[promotion_code_entry]  WITH CHECK ADD  CONSTRAINT [FKkcl5pj4d581qyc75u1g5j2t3s] FOREIGN KEY([reward_used_id])
REFERENCES [dbo].[reward_consumed] ([id])
GO
ALTER TABLE [dbo].[promotion_code_entry] CHECK CONSTRAINT [FKkcl5pj4d581qyc75u1g5j2t3s]
GO
ALTER TABLE [dbo].[promotion_code_reference]  WITH CHECK ADD  CONSTRAINT [FK1nn7psmlhgkagw6cqbquhgbyx] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[promotion_code_reference] CHECK CONSTRAINT [FK1nn7psmlhgkagw6cqbquhgbyx]
GO
ALTER TABLE [dbo].[promotion_code_reference]  WITH CHECK ADD  CONSTRAINT [FKb7xu3qif6vpqbetuf7xl7efg1] FOREIGN KEY([code_id])
REFERENCES [dbo].[promotion_code_master] ([id])
GO
ALTER TABLE [dbo].[promotion_code_reference] CHECK CONSTRAINT [FKb7xu3qif6vpqbetuf7xl7efg1]
GO
ALTER TABLE [dbo].[promotion_entry]  WITH CHECK ADD  CONSTRAINT [FK4nybhggqaym3wy30wersdnvt1] FOREIGN KEY([retailer_id])
REFERENCES [dbo].[retailer_master] ([id])
GO
ALTER TABLE [dbo].[promotion_entry] CHECK CONSTRAINT [FK4nybhggqaym3wy30wersdnvt1]
GO
ALTER TABLE [dbo].[promotion_entry]  WITH CHECK ADD  CONSTRAINT [FK5fsrv6kbdmie2g9k46xriru6m] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[promotion_entry] CHECK CONSTRAINT [FK5fsrv6kbdmie2g9k46xriru6m]
GO
ALTER TABLE [dbo].[promotion_entry]  WITH CHECK ADD  CONSTRAINT [FKel6sycclxfbawcki2uhqwabxk] FOREIGN KEY([prize_status])
REFERENCES [dbo].[status_master] ([id])
GO
ALTER TABLE [dbo].[promotion_entry] CHECK CONSTRAINT [FKel6sycclxfbawcki2uhqwabxk]
GO
ALTER TABLE [dbo].[promotion_entry]  WITH CHECK ADD  CONSTRAINT [FKr4ujj9b4boit32rhfmypb1gdy] FOREIGN KEY([status_id])
REFERENCES [dbo].[status_master] ([id])
GO
ALTER TABLE [dbo].[promotion_entry] CHECK CONSTRAINT [FKr4ujj9b4boit32rhfmypb1gdy]
GO
ALTER TABLE [dbo].[promotion_entry_errorlog]  WITH CHECK ADD  CONSTRAINT [FKduyoapo9cfy63soln35r5rplj] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[promotion_entry_errorlog] CHECK CONSTRAINT [FKduyoapo9cfy63soln35r5rplj]
GO
ALTER TABLE [dbo].[promotion_entry_errorlog]  WITH CHECK ADD  CONSTRAINT [FKhit9pmxr01246fms8pej0rnhk] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[promotion_entry_errorlog] CHECK CONSTRAINT [FKhit9pmxr01246fms8pej0rnhk]
GO
ALTER TABLE [dbo].[promotion_master]  WITH CHECK ADD  CONSTRAINT [FK6f7snfk0q17mjrsdticuuup1u] FOREIGN KEY([region_id])
REFERENCES [dbo].[region_master] ([id])
GO
ALTER TABLE [dbo].[promotion_master] CHECK CONSTRAINT [FK6f7snfk0q17mjrsdticuuup1u]
GO
ALTER TABLE [dbo].[promotion_master]  WITH CHECK ADD  CONSTRAINT [FK7crhhyf7ym23a61sq545poutr] FOREIGN KEY([promotion_cluster_id])
REFERENCES [dbo].[promotion_cluster] ([id])
GO
ALTER TABLE [dbo].[promotion_master] CHECK CONSTRAINT [FK7crhhyf7ym23a61sq545poutr]
GO
ALTER TABLE [dbo].[receipt_header]  WITH CHECK ADD  CONSTRAINT [FK66qkevyagcvgm657rmcibaoeh] FOREIGN KEY([receipt_id])
REFERENCES [dbo].[receipt_images] ([id])
GO
ALTER TABLE [dbo].[receipt_header] CHECK CONSTRAINT [FK66qkevyagcvgm657rmcibaoeh]
GO
ALTER TABLE [dbo].[receipt_images]  WITH CHECK ADD  CONSTRAINT [FKjpgaltsgb7cbke4ug340f75ws] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[receipt_images] CHECK CONSTRAINT [FKjpgaltsgb7cbke4ug340f75ws]
GO
ALTER TABLE [dbo].[receipt_product_details]  WITH CHECK ADD  CONSTRAINT [FKri9o1905a3pbbkdp5u2kh5wtd] FOREIGN KEY([receipt_id])
REFERENCES [dbo].[receipt_images] ([id])
GO
ALTER TABLE [dbo].[receipt_product_details] CHECK CONSTRAINT [FKri9o1905a3pbbkdp5u2kh5wtd]
GO
ALTER TABLE [dbo].[retailer_master_promotions]  WITH CHECK ADD  CONSTRAINT [FKh7rxjs71q9x881ujm3wr873xt] FOREIGN KEY([promotions_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[retailer_master_promotions] CHECK CONSTRAINT [FKh7rxjs71q9x881ujm3wr873xt]
GO
ALTER TABLE [dbo].[retailer_master_promotions]  WITH CHECK ADD  CONSTRAINT [FKi2jqf3rcn3inws8oxsfaugp2o] FOREIGN KEY([retailer_id])
REFERENCES [dbo].[retailer_master] ([id])
GO
ALTER TABLE [dbo].[retailer_master_promotions] CHECK CONSTRAINT [FKi2jqf3rcn3inws8oxsfaugp2o]
GO
ALTER TABLE [dbo].[reward_consumed]  WITH CHECK ADD  CONSTRAINT [FK9lnahb0i9nlu7egfaygwrv4ww] FOREIGN KEY([reward_id])
REFERENCES [dbo].[reward_master] ([id])
GO
ALTER TABLE [dbo].[reward_consumed] CHECK CONSTRAINT [FK9lnahb0i9nlu7egfaygwrv4ww]
GO
ALTER TABLE [dbo].[reward_consumed]  WITH CHECK ADD  CONSTRAINT [FKe0sffn8jh8e2og2jqb3gg57c7] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[reward_consumed] CHECK CONSTRAINT [FKe0sffn8jh8e2og2jqb3gg57c7]
GO
ALTER TABLE [dbo].[reward_consumed]  WITH CHECK ADD  CONSTRAINT [FKm494tbdlfbhfymxchjov8nlx5] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[reward_consumed] CHECK CONSTRAINT [FKm494tbdlfbhfymxchjov8nlx5]
GO
ALTER TABLE [dbo].[reward_master]  WITH CHECK ADD  CONSTRAINT [FK1bnyj2w8ja5qsybtsacfna3s1] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[reward_master] CHECK CONSTRAINT [FK1bnyj2w8ja5qsybtsacfna3s1]
GO
ALTER TABLE [dbo].[winner_selection_config]  WITH CHECK ADD  CONSTRAINT [FKrqx0nmvm81y2ur6wxmeedxsh1] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[winner_selection_config] CHECK CONSTRAINT [FKrqx0nmvm81y2ur6wxmeedxsh1]
GO
ALTER TABLE [dbo].[winner_selection_config_reference]  WITH CHECK ADD  CONSTRAINT [FKakrkdjbvfch90el3ewgn08hhp] FOREIGN KEY([config_id])
REFERENCES [dbo].[winner_selection_config] ([id])
GO
ALTER TABLE [dbo].[winner_selection_config_reference] CHECK CONSTRAINT [FKakrkdjbvfch90el3ewgn08hhp]
GO
ALTER TABLE [dbo].[winner_selection_config_reference]  WITH CHECK ADD  CONSTRAINT [FKps2pwb03agdig2q1ceu8fck70] FOREIGN KEY([promotion_id])
REFERENCES [dbo].[promotion_master] ([id])
GO
ALTER TABLE [dbo].[winner_selection_config_reference] CHECK CONSTRAINT [FKps2pwb03agdig2q1ceu8fck70]
GO
ALTER TABLE [dbo].[winners]  WITH CHECK ADD  CONSTRAINT [FKawq6bhw1v9pih099axpn8n6h5] FOREIGN KEY([promotion_entry_id])
REFERENCES [dbo].[promotion_entry] ([id])
GO
ALTER TABLE [dbo].[winners] CHECK CONSTRAINT [FKawq6bhw1v9pih099axpn8n6h5]
GO
USE [master]
GO
ALTER DATABASE [Poptopia] SET  READ_WRITE 
GO
