package PayloadGenerators;
import java.util.List;

public class PayloadData <T>{
    ///The payloads to be used during injection
    public final List<T> payload;

    ///The string to place inside the JarEntry when injection takes place
    public final String value;

    PayloadData(List<T> payloads, String value){
        this.payload=payloads;
        this.value=value;
    }


}
