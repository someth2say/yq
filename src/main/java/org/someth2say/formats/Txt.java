package org.someth2say.formats;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.someth2say.yqException;

import net.minidev.json.JSONArray;

public class Txt implements FormatMapper {

    public Txt(){
    }
    
	public final InputStream objectToInputStream(final Object obj) throws yqException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        String stringArray ;
        if (obj instanceof String) {
            stringArray = (String) obj;
        } else if (obj instanceof JSONArray) {
            stringArray = ((JSONArray) obj).stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new yqException("Only Array objects can be serialized to plain format (type is "+obj.getClass().getSimpleName()+")");
        }  
        
        out.writeBytes(stringArray.getBytes());
        return new ByteArrayInputStream(out.toByteArray());
    }

    public final Object inputStreamToObject(final InputStream plain) throws yqException {
        Class<Object> valueType = Object.class;
        return inputStreamToObject(plain, valueType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T inputStreamToObject(final InputStream plain, Class<T> valueType) throws yqException {

        if (valueType.isAssignableFrom(JSONArray.class)){
            JSONArray result = new JSONArray();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(plain))){
                while (reader.ready()) {
                    String line = reader.readLine();
                    result.add(line);
                }
            } catch (IOException e) {
                throw new yqException("Cannot read plan input",e);
            }
            
            return (T)result;

        } else {
            throw new yqException("Only Array objects can be deserialized to plain format.");
        }
    }

    @Override
    public Object stringToObject(String plain) throws yqException {
        Class<Object> valueType = Object.class;
        return stringToObject(plain, valueType);
    }

    @Override
    public <T> T stringToObject(String plain, Class<T> valueType) throws yqException {
        return inputStreamToObject(new ByteArrayInputStream(plain.getBytes()), valueType);
    }

}