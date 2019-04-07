#!bin/bash
sudo docker container stop phpmyadmin
sudo docker container stop mysql
sudo docker container rm phpmyadmin
sudo docker container rm mysql
sudo docker-compose up -d
sudo docker-compose logs -f