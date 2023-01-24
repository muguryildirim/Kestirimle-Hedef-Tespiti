# Proje Adı: Kestririmle-Hedef-Tespiti
## Proje Amacı: 1000x1000’lik bir alan üzerinde konumlanacak iki sensörün, uygulama üzerinden aldığı sensor orjin ve hedef kerteriz bilgilerini kullanarak hedefin kartezyen koordinatının tespit edilmesi.

### Kullanılan Teknolojiler:
•	Maven
•	Java 
•	Apache Kafka
•	Spring 
    o	Spring Boot
    o	Spring Kafka
    
### Kafka Mimarisi
Producer (Üretici): Kafka’ya veri sağlayan kaynaklardır. Sunucular, bulut verileri, makine durumu verileri gibi daha pek çok kaynaktan alınan veriler olabilir. Bu veriler, veri formatlarıyla birlikte Kafka Cluster yapısı içine gönderilir (Push edilir).
Topic (Konu): Producer’lardan gelen veriler Topic içine gönderilir. 
Consumer (Tüketici): Topic ve Partitionlar’da tutulan verileri okur (pull eder) ve verilen komutları yerine getirir.
### Konsol Komutları ve Kafka Bağlantısı
ZooKeeper, dağıtılmış sistemlerde hizmet senkronizasyonu için ve bir adlandırma kaydı olarak kullanılır. ZooKeeper, Kafka kümesindeki düğümlerin durumunu izlemek ve Kafka konularının ve mesajlarının bir listesini tutmak için kullanılır.
``` 
.\bin\windows\zookeeper-server-start.bat .\config\zookeper.properties
```
Kafka Sunucu Başlatımı
```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```
Topic Oluşturumu
#### .\bin\windows\kafka-console-consumer.bat --zookeper localhost:2181 --replication-factor 1 --partitions 1--topic Location_json
Kafka’ya Mesaj Gönderme
#### .\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic Location_json
Kafka’dan Mesaj Okuma
#### .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic Location_json --from-beginning
