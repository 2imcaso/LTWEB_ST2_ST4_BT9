package security.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service public class ImageService {
 public record Upload(String url,String publicId){}
 private final Cloudinary cloudinary;@Value("${app.cloudinary.enabled:false}")private boolean enabled;
 public ImageService(Cloudinary c){cloudinary=c;}
 public Upload upload(MultipartFile file)throws IOException{if(file==null||file.isEmpty())return null;if(!enabled)throw new IllegalStateException("Cloudinary chưa được bật trong .env.");var r=cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap("folder","vd3-products","resource_type","image"));return new Upload(r.get("secure_url").toString(),r.get("public_id").toString());}
 public void delete(String id)throws IOException{if(enabled&&id!=null&&!id.isBlank())cloudinary.uploader().destroy(id,ObjectUtils.emptyMap());}
}
