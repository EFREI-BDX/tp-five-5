package com.jad.efreifive.managefield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import com.jad.efreifive.managefield.persistence.ActiveFieldViewRecord;
import com.jad.efreifive.managefield.persistence.BlockingReservationViewRecord;
import com.jad.efreifive.managefield.persistence.FieldStatusViewRecord;
import com.jad.efreifive.managefield.persistence.FieldViewRecord;
import com.jad.efreifive.managefield.persistence.ReservationStatusViewRecord;

@SpringBootApplication
@EntityScan(basePackageClasses = {
    ActiveFieldViewRecord.class,
    BlockingReservationViewRecord.class,
    FieldStatusViewRecord.class,
    FieldViewRecord.class,
    ReservationStatusViewRecord.class
})
public class ManagefieldApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagefieldApplication.class, args);
    }

}
