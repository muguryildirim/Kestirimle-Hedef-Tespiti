package com.producer;

import com.producer.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    protected static int[] getRandomVect(int minX, int minY, int maxX, int maxY)
    {
        int[] result=new int[2];
        result[0]=(int) (Math.random()*(maxX-minX)+minX);
        result[1]=(int) (Math.random()*(maxY-minY)+minY);
        return result;
    }
    protected static double bearing(int lat1, int lon1, int lat2, int lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y = Math.sin(longDiff)*Math.cos(latitude2);
        double x = Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    @Autowired
    private KafkaTemplate<String, Sensor> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> bearingKafkaTemplate;

    private static final String TOPIC = "Location_json";

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        Double sensor1BearingInformation = 0d;
        Double sensor2BearingInformation = 0d;

        int[] target = getRandomVect(-500, -500, 500, 500);

        int[] sensorVector1 = getRandomVect(-500, -500, 500, 500);
        int[] sensorVector2 = getRandomVect(-500, -500, 500, 500);

        sensor1BearingInformation = bearing(sensorVector1[1], sensorVector1[0], target[1], target[0]);
        sensor2BearingInformation = bearing(sensorVector2[1], sensorVector2[0], target[1], target[0]);

        kafkaTemplate.send(TOPIC, new Sensor("1", sensorVector1[0], sensorVector1[1],sensor1BearingInformation.intValue(),"2", sensorVector2[0], sensorVector2[1],sensor2BearingInformation.intValue()));

    }
}