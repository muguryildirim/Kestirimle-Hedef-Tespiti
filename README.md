# Proje Adı: Kestirimle-Hedef-Tespiti
## Proje Amacı: 1000x1000’lik bir alan üzerinde konumlanacak iki sensörün, uygulama üzerinden aldığı sensor orjin ve hedef kerteriz bilgilerini kullanarak hedefin kartezyen koordinatının tespit edilmesi.

### Kullanılan Teknolojiler:
•	Apache Kafka
•	Java 
•	Maven
•	Spring (Dependencies (Spring Boot, Spring Kafka))
    
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
```
.\bin\windows\kafka-console-consumer.bat --zookeper localhost:2181 --replication-factor 1 --partitions 1--topic Location_json
```
Kafka’ya Mesaj Gönderme
```
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic Location_json
```
Kafka’dan Mesaj Okuma
```
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic Location_json --from-beginning
```

### Java - Kafka Consumer:
Mesajları consume etmek için bir ConsumerFactory ve bir KafkaListenerContainerFactory yapılandırmamız gerekiyor. Bu Beansler, Spring Beans factorysinde mevcut olduğunda, POJO tabanlı consumerlar kullanılarak yapılandırılabilir.
```
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
```
### Tüketim (Consuming) Mesajları:
Bir topic için, her biri farklı bir grup kimliğine sahip birden çok dinleyici uygulayabiliriz. Ayrıca, bir tüketici çeşitli topiclerdeki mesajları dinleyebilir:
JSON, bir dizideki nesneleri kodlayan bir biçimdir. Seri hale getirme, bir nesneyi o dizgeye dönüştürmek anlamına gelir ve seri hale getirme (deserialization) onun ters işlemidir (dizeyi dönüştür -> nesne).

Verileri iletirken veya bir dosyada saklarken, verilerin bayt dizileri olması gerekir, ancak karmaşık nesneler nadiren bu biçimdedir. Seri hale getirme, bu tür kullanımlar için bu karmaşık nesneleri bayt dizilerine dönüştürebilir. Bayt dizileri iletildikten sonra, alıcının orijinal nesneyi bayt dizisinden kurtarması gerekecektir. Bu seri hale getirme olarak bilinir.
```
@KafkaListener(topics = "Location_json", groupId = "group_json",
            containerFactory = "sensorKafkaListenerFactory")
    public void consumeJson(Sensor sensor) {
        double[] eq1 = null;
        double[] eq2 = null;


        System.out.println("Consumed JSON Message: " + sensor);
        System.out.println("Sensor Name: " + sensor.getName1());
        System.out.println("X1: " + sensor.getPositionX1());
        System.out.println("Y1: " + sensor.getPositionY1());
        System.out.println("Bearing Angle1:" + sensor.getBearingAngle1());
        System.out.println("----------------------------------------------");
        System.out.println("Sensor Name: " + sensor.getName2());
        System.out.println("X2: " + sensor.getPositionX2());
        System.out.println("Y2: " + sensor.getPositionY2());
        System.out.println("Bearing Angle2:" + sensor.getBearingAngle2());

        eq1 = generateEquation(sensor.getBearingAngle1(), sensor.getPositionX1(), sensor.getPositionY1());
        eq2 = generateEquation(sensor.getBearingAngle2(), sensor.getPositionX2(), sensor.getPositionY2());

        double[] calculatedPoints = calculateIntersectionPoints(eq1, eq2);

        System.out.println("Target X: " + calculatedPoints[0]);
        System.out.println("Target Y: " + calculatedPoints[1]);
    }
```
### Producer (Üretici) Mesajları
Mesaj oluşturmak için önce bir ProducerFactory yapılandırmamız gerekiyor. Bu, Kafka Producer örnekleri (instance) oluşturma stratejisini ayarlar.
Ardından, bir Üretici örneğini saran ve Kafka konularına mesaj göndermek için kolaylık sağlayan yöntemler sağlayan bir KafkaTemplate'e ihtiyacımız var.
```
@Bean
    public ProducerFactory<String, Sensor> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
  }
  ```


  
