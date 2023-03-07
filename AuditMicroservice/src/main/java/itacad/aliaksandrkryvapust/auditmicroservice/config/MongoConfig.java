package itacad.aliaksandrkryvapust.auditmicroservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EntityAuxiliaryFields;
import lombok.NonNull;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.util.UUID;

@Configuration
@EnableMongoRepositories(basePackages = "itacad.aliaksandrkryvapust.auditmicroservice.repository.api")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected @NonNull String getDatabaseName() {
        return "audit";
    }

    @Override
    public @NonNull MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://admin:pass@localhost:27017/");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public BeforeConvertCallback<EntityAuxiliaryFields> beforeSaveCallback() {
        return (entity, collection) -> {
            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID());
            }
            if (entity.getDtCreate() == null) {
                entity.setDtCreate(Instant.now());
            }
            return entity;
        };
    }
}
