CREATE TABLE IF NOT EXISTS "__EFMigrationsLock" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK___EFMigrationsLock" PRIMARY KEY,
    "Timestamp" TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "__EFMigrationsHistory" (
    "MigrationId" TEXT NOT NULL CONSTRAINT "PK___EFMigrationsHistory" PRIMARY KEY,
    "ProductVersion" TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "Actors" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_Actors" PRIMARY KEY AUTOINCREMENT,
    "ProfilePictureURL" TEXT NOT NULL,
    "FullName" TEXT NOT NULL,
    "Bio" TEXT NOT NULL
);
CREATE TABLE sqlite_sequence(name,seq);
CREATE TABLE IF NOT EXISTS "AspNetRoles" (
    "Id" TEXT NOT NULL CONSTRAINT "PK_AspNetRoles" PRIMARY KEY,
    "Name" TEXT NULL,
    "NormalizedName" TEXT NULL,
    "ConcurrencyStamp" TEXT NULL
);
CREATE TABLE IF NOT EXISTS "AspNetUsers" (
    "Id" TEXT NOT NULL CONSTRAINT "PK_AspNetUsers" PRIMARY KEY,
    "FullName" TEXT NULL,
    "UserName" TEXT NULL,
    "NormalizedUserName" TEXT NULL,
    "Email" TEXT NULL,
    "NormalizedEmail" TEXT NULL,
    "EmailConfirmed" INTEGER NOT NULL,
    "PasswordHash" TEXT NULL,
    "SecurityStamp" TEXT NULL,
    "ConcurrencyStamp" TEXT NULL,
    "PhoneNumber" TEXT NULL,
    "PhoneNumberConfirmed" INTEGER NOT NULL,
    "TwoFactorEnabled" INTEGER NOT NULL,
    "LockoutEnd" TEXT NULL,
    "LockoutEnabled" INTEGER NOT NULL,
    "AccessFailedCount" INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS "Cinemas" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_Cinemas" PRIMARY KEY AUTOINCREMENT,
    "Logo" TEXT NOT NULL,
    "Name" TEXT NOT NULL,
    "Description" TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "Producers" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_Producers" PRIMARY KEY AUTOINCREMENT,
    "ProfilePictureURL" TEXT NOT NULL,
    "FullName" TEXT NOT NULL,
    "Bio" TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "AspNetRoleClaims" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_AspNetRoleClaims" PRIMARY KEY AUTOINCREMENT,
    "RoleId" TEXT NOT NULL,
    "ClaimType" TEXT NULL,
    "ClaimValue" TEXT NULL,
    CONSTRAINT "FK_AspNetRoleClaims_AspNetRoles_RoleId" FOREIGN KEY ("RoleId") REFERENCES "AspNetRoles" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "AspNetUserClaims" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_AspNetUserClaims" PRIMARY KEY AUTOINCREMENT,
    "UserId" TEXT NOT NULL,
    "ClaimType" TEXT NULL,
    "ClaimValue" TEXT NULL,
    CONSTRAINT "FK_AspNetUserClaims_AspNetUsers_UserId" FOREIGN KEY ("UserId") REFERENCES "AspNetUsers" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "AspNetUserLogins" (
    "LoginProvider" TEXT NOT NULL,
    "ProviderKey" TEXT NOT NULL,
    "ProviderDisplayName" TEXT NULL,
    "UserId" TEXT NOT NULL,
    CONSTRAINT "PK_AspNetUserLogins" PRIMARY KEY ("LoginProvider", "ProviderKey"),
    CONSTRAINT "FK_AspNetUserLogins_AspNetUsers_UserId" FOREIGN KEY ("UserId") REFERENCES "AspNetUsers" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "AspNetUserRoles" (
    "UserId" TEXT NOT NULL,
    "RoleId" TEXT NOT NULL,
    CONSTRAINT "PK_AspNetUserRoles" PRIMARY KEY ("UserId", "RoleId"),
    CONSTRAINT "FK_AspNetUserRoles_AspNetRoles_RoleId" FOREIGN KEY ("RoleId") REFERENCES "AspNetRoles" ("Id") ON DELETE CASCADE,
    CONSTRAINT "FK_AspNetUserRoles_AspNetUsers_UserId" FOREIGN KEY ("UserId") REFERENCES "AspNetUsers" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "AspNetUserTokens" (
    "UserId" TEXT NOT NULL,
    "LoginProvider" TEXT NOT NULL,
    "Name" TEXT NOT NULL,
    "Value" TEXT NULL,
    CONSTRAINT "PK_AspNetUserTokens" PRIMARY KEY ("UserId", "LoginProvider", "Name"),
    CONSTRAINT "FK_AspNetUserTokens_AspNetUsers_UserId" FOREIGN KEY ("UserId") REFERENCES "AspNetUsers" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Orders" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_Orders" PRIMARY KEY AUTOINCREMENT,
    "Email" TEXT NULL,
    "UserId" TEXT NULL,
    CONSTRAINT "FK_Orders_AspNetUsers_UserId" FOREIGN KEY ("UserId") REFERENCES "AspNetUsers" ("Id")
);
CREATE TABLE IF NOT EXISTS "Movies" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_Movies" PRIMARY KEY AUTOINCREMENT,
    "Name" TEXT NULL,
    "Description" TEXT NULL,
    "Price" REAL NOT NULL,
    "ImageURL" TEXT NULL,
    "StartDate" TEXT NOT NULL,
    "EndDate" TEXT NOT NULL,
    "MovieCategory" INTEGER NOT NULL,
    "CinemaId" INTEGER NOT NULL,
    "ProducerId" INTEGER NOT NULL,
    CONSTRAINT "FK_Movies_Cinemas_CinemaId" FOREIGN KEY ("CinemaId") REFERENCES "Cinemas" ("Id") ON DELETE CASCADE,
    CONSTRAINT "FK_Movies_Producers_ProducerId" FOREIGN KEY ("ProducerId") REFERENCES "Producers" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Actors_Movies" (
    "MovieId" INTEGER NOT NULL,
    "ActorId" INTEGER NOT NULL,
    CONSTRAINT "PK_Actors_Movies" PRIMARY KEY ("ActorId", "MovieId"),
    CONSTRAINT "FK_Actors_Movies_Actors_ActorId" FOREIGN KEY ("ActorId") REFERENCES "Actors" ("Id") ON DELETE CASCADE,
    CONSTRAINT "FK_Actors_Movies_Movies_MovieId" FOREIGN KEY ("MovieId") REFERENCES "Movies" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "OrderItems" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_OrderItems" PRIMARY KEY AUTOINCREMENT,
    "Amount" INTEGER NOT NULL,
    "Price" REAL NOT NULL,
    "MovieId" INTEGER NOT NULL,
    "OrderId" INTEGER NOT NULL,
    CONSTRAINT "FK_OrderItems_Movies_MovieId" FOREIGN KEY ("MovieId") REFERENCES "Movies" ("Id") ON DELETE CASCADE,
    CONSTRAINT "FK_OrderItems_Orders_OrderId" FOREIGN KEY ("OrderId") REFERENCES "Orders" ("Id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShoppingCartItems" (
    "Id" INTEGER NOT NULL CONSTRAINT "PK_ShoppingCartItems" PRIMARY KEY AUTOINCREMENT,
    "MovieId" INTEGER NULL,
    "Amount" INTEGER NOT NULL,
    "ShoppingCartId" TEXT NULL,
    CONSTRAINT "FK_ShoppingCartItems_Movies_MovieId" FOREIGN KEY ("MovieId") REFERENCES "Movies" ("Id")
);
CREATE INDEX "IX_Actors_Movies_MovieId" ON "Actors_Movies" ("MovieId");
CREATE INDEX "IX_AspNetRoleClaims_RoleId" ON "AspNetRoleClaims" ("RoleId");
CREATE UNIQUE INDEX "RoleNameIndex" ON "AspNetRoles" ("NormalizedName");
CREATE INDEX "IX_AspNetUserClaims_UserId" ON "AspNetUserClaims" ("UserId");
CREATE INDEX "IX_AspNetUserLogins_UserId" ON "AspNetUserLogins" ("UserId");
CREATE INDEX "IX_AspNetUserRoles_RoleId" ON "AspNetUserRoles" ("RoleId");
CREATE INDEX "EmailIndex" ON "AspNetUsers" ("NormalizedEmail");
CREATE UNIQUE INDEX "UserNameIndex" ON "AspNetUsers" ("NormalizedUserName");
CREATE INDEX "IX_Movies_CinemaId" ON "Movies" ("CinemaId");
CREATE INDEX "IX_Movies_ProducerId" ON "Movies" ("ProducerId");
CREATE INDEX "IX_OrderItems_MovieId" ON "OrderItems" ("MovieId");
CREATE INDEX "IX_OrderItems_OrderId" ON "OrderItems" ("OrderId");
CREATE INDEX "IX_Orders_UserId" ON "Orders" ("UserId");
CREATE INDEX "IX_ShoppingCartItems_MovieId" ON "ShoppingCartItems" ("MovieId");
