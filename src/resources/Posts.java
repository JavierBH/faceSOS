package resources;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "posts")

public class Posts {
	private ArrayList<PostResource> posts;
	public Posts(){
		this.posts = new ArrayList<PostResource>();
	}
	@XmlElement(name="post") 
	public ArrayList<PostResource> getPosts(){
		return this.posts;
	}
}
