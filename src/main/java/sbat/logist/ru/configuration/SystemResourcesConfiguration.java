package sbat.logist.ru.configuration;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sbat.logist.ru.configuration.property.ParserProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Getter
public class SystemResourcesConfiguration {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private Path jsonDataDirPath;
    private Path backupDirPath;
    private Path responseDirPath;
    private Path logsDirPath;

    @Autowired
    public SystemResourcesConfiguration(ParserProperties configuration) {
        jsonDataDirPath = Paths.get(configuration.getJsonDataDir());
        backupDirPath = Paths.get(configuration.getBackupDir());
        responseDirPath = Paths.get(configuration.getResponseDir());
        logsDirPath = Paths.get(configuration.getLogsDir());

        validatePathIsDirectory(jsonDataDirPath);
        validatePathIsDirectory(backupDirPath);
        validatePathIsDirectory(responseDirPath);
        validatePathIsDirectory(logsDirPath);
    }

    private void validatePathIsDirectory(Path path) {
        if (!path.toFile().exists()) {
            final String error = String.format("directory %s is not exist", path.toString());
            logger.error(error);
            throw new IllegalStateException(error);
        }
        if (!path.toFile().isDirectory()) {
            final String errorMessage = String.format("path %s must be a directory", path.toString());
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    @Bean
    @Qualifier("responseDirPath")
    public Path responseDirPath() {
        return responseDirPath;
    }

    @Bean
    @Qualifier("backupDirPath")
    public Path backupDirPath() {
        return backupDirPath;
    }

    @Bean
    @Qualifier("jsonDataDirPath")
    public Path jsonDataDirPath() {
        return jsonDataDirPath;
    }

    @Bean
    @Qualifier("logsDirPath")
    public Path logsDirPath() {
        return logsDirPath;
    }
}
