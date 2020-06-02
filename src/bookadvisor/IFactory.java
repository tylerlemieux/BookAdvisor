package bookadvisor;

import java.util.HashMap;

public interface IFactory<T> {
	public T Create(HashMap<String, String> properties);
}
