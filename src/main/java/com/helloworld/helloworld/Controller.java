package com.helloworld.helloworld;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.aws.mcs.auth.SigV4AuthProvider;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/hello")
    public String sendMessage() {
        return select().toString();
    }

    public List<Table> select(){
        List<Table> list = new ArrayList<>();

        SigV4AuthProvider provider = new SigV4AuthProvider("sa-east-1");

        List<InetSocketAddress> contactPoints = Collections.singletonList(new InetSocketAddress("cassandra.sa-east-1.amazonaws.com", 9142));

        try (CqlSession session = CqlSession.builder()
                .addContactPoints(contactPoints)
                .withAuthProvider(provider)
                .withLocalDatacenter("sa-east-1")
                .build()) {

            ResultSet rs = session.execute("select * from keyspace_bff.table_bff");

            for (Row row : rs) {
                logger.info("TESTE -> " + row.getString("id") + " " + row.getString("name") + " " + row.getString("description"));
                list.add(new Table(row.getString("id"), row.getString("name"), row.getString("description")));
            }

            System.out.println("Acabou!!!");

        }catch (Exception e){
            logger.error(e.getMessage());
            logger.info(e.getMessage());
            list.add(new Table("NADA", "NADA", "NADA"));
        }finally {
            return list;
        }
    }
}
