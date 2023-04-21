# Init steps - DB

- Spin up container with database - `docker-compose up -d`
- Connect to database - `docker exec -it postgres bash`
- Connect to postgresql - `psql -U akamai`
- Create database - `CREATE DATABASE akamai_posts;`
- Check if database is created - `\l`

# Assumptions

- Assumed that listing posts do not increment views
- Only reading specific post increments view count
- Assumed that there is no need for authentication
- Assumed that author is not crucial for this task, so it is represented by string name
- For simple project file structure is organised by feature, not by type of file (e.g. controller, service, etc.)

# To be considered

- Incrementing view count could potentially decrease performance in case of high load especially if there are a high
  volume of reads happening concurrently. In this case, it would be better to use a queue to increment view count in the
  background. Or use distributed caching utilising Redis? and periodically write counts back to database in batches.
