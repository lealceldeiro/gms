package com.gms.util.response;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ResponseDataTest {

	private static final String KEY1 = "key1";
	private static final String VALUE1 = "value1";
	private static final String KEY_TEXT = "key";
	private static final String VALUE_TEXT = "value";
	private ResponseData<String> entity;
	private ResponseData<String> entity2;

	@Test
	public void responseDataAllArgsConstructor() {
		ResponseData<String> rd = new ResponseData<>(KEY1, VALUE1);
		assertEquals(ReflectionTestUtils.getField(rd, KEY_TEXT), KEY1);
		assertEquals(ReflectionTestUtils.getField(rd, VALUE_TEXT), VALUE1);
	}

	@Test
	public void getKey() {
		cleanEntity();

		ReflectionTestUtils.setField(entity, "key", KEY1);
		assertEquals(entity.getKey(), KEY1);
	}

	@Test
	public void getValue() {
		cleanEntity();

		ReflectionTestUtils.setField(entity, "value", VALUE1);
		assertEquals(entity.getValue(), VALUE1);
	}

	@Test
	public void setKey() {
		cleanEntity();

		entity.setKey(KEY1);
		assertEquals(KEY1, String.valueOf(ReflectionTestUtils.getField(entity, "key")));
	}

	@Test
	public void setValue() {
		cleanEntity();

		entity.setValue(VALUE1);
		assertEquals(VALUE1, String.valueOf(ReflectionTestUtils.getField(entity, "value")));
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
		entity = new ResponseData<>();
		assertEntityValidity(entity);
	}

	private void cleanEntity2() {
		entity2 = new ResponseData<>();
		assertEntityValidity(entity2);
	}

	private void assertEntityValidity(ResponseData<?> entity) {
		assertNull(ReflectionTestUtils.getField(entity, KEY_TEXT));
		assertNull(ReflectionTestUtils.getField(entity, VALUE_TEXT));
	}

	private void prepareEntitiesForEqualityTest() {
		cleanEntity();
		cleanEntity2();

		prepareEntityForEqualityTest(entity);
		prepareEntityForEqualityTest(entity2);
	}

	private void prepareEntityForEqualityTest(ResponseData<?> e) {
		ReflectionTestUtils.setField(e, KEY_TEXT, KEY1);
		ReflectionTestUtils.setField(e, VALUE_TEXT, VALUE1);
	}
}