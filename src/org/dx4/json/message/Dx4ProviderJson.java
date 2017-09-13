package org.dx4.json.message;

public class Dx4ProviderJson
{
	private long id;
	private Character code;
	private String name;
	private byte[] rawImage;
	private String image;
	private String url;
	
	public Dx4ProviderJson()
	{
	}
	
	public Character getCode() {
		return code;
	}
	public void setCode(Character code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Dx4Provider [id=" + id + ", code=" + code + ", name=" + name
				+ "]";
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public byte[] getRawImage() {
		return rawImage;
	}

	public void setRawImage(byte[] rawImage) {
		this.rawImage = rawImage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
