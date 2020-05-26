package org.redhattraining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

public class yaml implements FormatMapper {
    private ObjectMapper yamlMapper;

    public yaml(ObjectMapper yamlMapper) {
        this.yamlMapper=yamlMapper;
	}

    public yaml(){
        this(new YAMLMapper());
    }
    
	public final InputStream objectToInputStream(final Object obj) throws yqException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            yamlMapper.writeValue(out, obj);
        } catch (final JsonGenerationException e) {
            throw new yqException("Can not generate YAML", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not encode YAML", e);
        } catch (final IOException e) {
            throw new yqException("Can not write YAML", e);
        }
        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }

    public final Object inputStreamToObject(final InputStream json) throws yqException {
        try {
            return yamlMapper.readValue(json, Object.class);
        } catch (JsonParseException e) {
            throw new yqException("Can not parse YAML", e);
        } catch (JsonMappingException e) {
            throw new yqException("Can not decode YAML", e);
        } catch (IOException e) {
            throw new yqException("Can not read YAML", e);
        }
    }

    @Override
    public Object stringToObject(String yaml) throws yqException {
        try {
            return yamlMapper.readValue(yaml, Object.class);
        } catch (JsonParseException e) {
            throw new yqException("Can not parse YAML", e);
        } catch (JsonMappingException e) {
            throw new yqException("Can not decode YAML", e);
        } catch (IOException e) {
            throw new yqException("Can not read YAML", e);
        }
    }

    public final Format getFormat(){
		return Format.YAML;
	}


}