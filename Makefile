build-all:
	docker-compose up --build -d

stop:
	docker-compose stop

start:
	docker-compose up -d

start-db:
	docker-compose up -d admin-video-catalog-api-mysql
