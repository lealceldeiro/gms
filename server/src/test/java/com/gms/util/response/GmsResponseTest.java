package com.gms.util.response;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gms.Application;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsResponseTest {

	private static final String MESSAGE_VALUE = "message1";
	private static final String MESSAGE_TEXT = "message";
	private final ResponseData<?>[] rd = { new ResponseData<String>() };
	private GmsResponse entity;
	private GmsResponse entity2;

	@Test
	public void GmsResponseResponseData() {
		ResponseData<String> rd = new ResponseData<>();
		rd.setKey("key1");
		rd.setValue("value1");
		ResponseData<?>[] data = { rd };
		GmsResponse e = new GmsResponse(data);
		assertEquals(ReflectionTestUtils.getField(e, "data"), data);
		assertEquals("", ReflectionTestUtils.getField(e, MESSAGE_TEXT));
	}

	@Test
	public void getMessage() {
		cleanEntity();

		ReflectionTestUtils.setField(entity, MESSAGE_TEXT, MESSAGE_VALUE);
		assertEquals(entity.getMessage(), MESSAGE_VALUE);
	}

	@Test
	public void getData() {
		cleanEntity();

		ReflectionTestUtils.setField(entity, "data", rd);
		assertArrayEquals(entity.getData(), rd);
	}

	@Test
	public void setMessage() {
		cleanEntity();

		entity.setMessage(MESSAGE_VALUE);
		assertEquals(MESSAGE_VALUE, String.valueOf(ReflectionTestUtils.getField(entity, MESSAGE_TEXT)));

	}

	@Test
	public void equalsTest() {
		cleanEntity();
		prepareEntitiesForEqualityTest();

		assertEquals(entity, entity2);
	}

	@Test
	public void hashCodeTest() {
		prepareEntitiesForEqualityTest();

		assertEquals(entity.hashCode(), entity2.hashCode());
	}

	@Test
	public void toStringTest() {
		prepareEntitiesForEqualityTest();
		assertEquals(entity.toString(), entity2.toString());
	}

	private void cleanEntity() {
		entity = new GmsResponse("", null);
		assertEntityValidity(entity);
	}

	private void cleanEntity2() {
		entity2 = new GmsResponse("", null);
		assertEntityValidity(entity2);
	}

	private void assertEntityValidity(GmsResponse entity) {
		Object field = ReflectionTestUtils.getField(entity, "message");
		assertNotNull(field);
		assertEquals("", field.toString());
		assertNull(ReflectionTestUtils.getField(entity, "data"));
	}

	private void prepareEntitiesForEqualityTest() {
		cleanEntity();
		cleanEntity2();

		prepareEntityForEqualityTest(entity);
		prepareEntityForEqualityTest(entity2);
	}

	private void prepareEntityForEqualityTest(GmsResponse e) {
		ReflectionTestUtils.setField(e, "message", MESSAGE_VALUE);
		ReflectionTestUtils.setField(e, "data", rd);
	}
}