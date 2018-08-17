CREATE USER '${binding.credentials.username}' IDENTIFIED BY '${binding.credentials.password}';
GRANT ALL PRIVILEGES ON `${instance.config.database}`.* TO '${binding.credentials.username}'@'%';
FLUSH PRIVILEGES