🧭 Customer Onboarding Experience Mapper
📋 Overview

The Customer Onboarding Experience Mapper is a Java console-based backend application that helps record and manage the customer onboarding process.
It connects directly to MongoDB using the official MongoDB Java Driver (v4.11.0), allowing you to store customer details, onboarding stages, and progress efficiently.

🚀 Features

🗂️ Add, update, and delete customer records.

⚙️ Track onboarding stages (e.g., registration, KYC, activation).

📊 Retrieve and display onboarding progress.

💬 Store customer feedback.

🧩 Built using the MongoDB Java Driver — no external frameworks required.

🏗️ Technology Stack
Component	Description
Language	Java (JDK 17 or higher)
Database	MongoDB (local or Atlas)
Libraries	bson-4.11.0.jar, mongodb-driver-core-4.11.0.jar, mongodb-driver-sync-4.11.0.jar
Type	Console-based Application
📂 Folder Structure
CustomerOnboardingExperienceMapper/
│
├── CustomerOnboardingExperienceMapper.java     # Main Java source file
├── CustomerOnboardingExperienceMapper.class    # Compiled class
├── CLASS PATH EXECUTION AND COMPILE.txt        # Contains classpath & compile instructions
├── bson-4.11.0.jar                             # MongoDB dependency
├── mongodb-driver-core-4.11.0.jar
├── mongodb-driver-sync-4.11.0.jar
└── README.md                                   # You are here

⚙️ Setup & Execution
🖥️ Prerequisites

Java JDK 17+ installed (java -version)

MongoDB running locally or accessible remotely

All MongoDB driver JARs in the same folder as your .java file
