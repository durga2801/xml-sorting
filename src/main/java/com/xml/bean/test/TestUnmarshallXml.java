package com.xml.bean.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.xml.bean.Attribute;
import com.xml.bean.Column;
import com.xml.bean.Table;

public class TestUnmarshallXml {

	public static void main(String[] args) throws Exception {
		
		File file = new File("src/main/resources/xml/data.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Table.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Table table = (Table) jaxbUnmarshaller.unmarshal(file);
		
		table.getColumn().sort(Comparator.comparing(Column::getName)
                .thenComparing(Column::getType));
		
		table.getRow().forEach(row->{
			row.getAttribute().sort(Comparator.comparing(Attribute::getName)
					.thenComparing(Attribute::getValue));
		});
		
		
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
		Writer writer = new StringWriter();
		marshaller.marshal(table, writer);
		
		System.out.println(writer.toString());
		
		File file2 = new File("src/main/resources/xml-sorted/data.xml");
		file2.createNewFile();
		//FileOutputStream oFile = new FileOutputStream(file2, false); 
		marshaller.marshal(table, file2); 
		
		System.out.println(table);
		
	}

	public static String readFileAsString(String file) throws Exception {
		return new String(Files.readAllBytes(Paths.get(file)));
	}

}
