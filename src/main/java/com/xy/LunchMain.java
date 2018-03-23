package com.xy;

import java.io.UnsupportedEncodingException;

import com.google.protobuf.Any;
import com.xy.NestGen.Nest;
import com.xy.NestGen.SingleInt;
import com.xy.TwoDiffGen.TwoDiff;
import com.xy.TwoIntGen.TwoInt;

/**
 * 
 * @author Xinyuan.Yan
 *
 */
public class LunchMain {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("---------------------------------------------------------------------------------------------");
		System.out.println("(field_number << 3) | wire_type");
		System.out.println("---------------------------------------------------------------------------------------------");
		testSingleInt();
		System.out.println("---------------------------------------------------------------------------------------------");
		testTwoInt();
		System.out.println("---------------------------------------------------------------------------------------------");
		testTwoDiff();
		System.out.println("---------------------------------------------------------------------------------------------");
		testNestMessage();
		System.out.println("---------------------------------------------------------------------------------------------");
		testAny();
		System.out.println("---------------------------------------------------------------------------------------------");
	}
	
	public static void testSingleInt() {
		System.out.println("single int start ...");
		//1 : 00000001
		System.out.print("i : " + 1 + " -> ");
		display(singleInt(1));
		//256 + 32 + 8 + 4 : 100101100
		System.out.print("i : " + 300+ " -> ");
		display(singleInt(300));
		//16384 + 4096 + 1: 101000000000001
		System.out.print("i : " + 20481+ " -> ");
		display(singleInt(20481));
		System.out.println("single int end ...");
	}
	
	public static void testTwoInt() {
		System.out.println("Two int start ...");
		System.out.print("i : " + 1 + ", j : " + 1 + " -> ");
		display(twoInt(1, 1));
		System.out.print("i : " + 300 + ", j : " + 300 + " -> ");
		display(twoInt(300, 300));
		System.out.print("i : " + 20481 + ", j : " + 20481 + " -> ");
		display(twoInt(1, 20481));
		System.out.println("Two int end ...");
	}
	
	public static void testTwoDiff() throws UnsupportedEncodingException {
		System.out.println("Two diff start ...");
		//
		System.out.print("i : " + 1 + ", s : " + "\"1\"" + " -> ");
		display(twoDiff(1, "1"));
		System.out.print("i : " + 300 + ", s : " + "\"127\"" + " -> ");
		display(twoDiff(300, "127"));
		System.out.print("i : " + 20481 + ", s : " + "\"300\"" + " -> ");
		display(twoDiff(20481, "300"));
		System.out.println("Two diff end ...");
	}
	
	public static void testNestMessage() {
		System.out.println("Nest message start ...");
		System.out.print("i : " + 1 + ", ii1 : " + 1 + " -> ");
		display(nestSingle(1, 1));
		System.out.print("i : " + 300 + ", ii1 : " + 300 + " -> ");
		display(nestSingle(300, 300));
		System.out.print("i : " + 1 + ", ii1 : " + 20481 + " -> ");
		display(nestSingle(1, 20481));
		System.out.println("Nest message end ...");
	}
	
	public static void testAny() {
		System.out.println("Any test start ...");
		System.out.print("i : " + 1 + ", typeurl : ");
		display(singleIntAny(1));
		System.out.println("Any test end ...");
	}
	
	public static byte[] singleInt(int i) {
		SingleInt pb = SingleInt.newBuilder()
				.setI1(i)
				.build();
		return pb.toByteArray();
	}
	
	public static byte[] singleIntAny(int i) {
		SingleInt pb = SingleInt.newBuilder()
				.setI1(i)
				.build();
		Any any = Any.pack(pb);
		String url = getUrl(pb);
		System.out.print(url + ", ASCII : ");
		for (byte dt : url.substring(0, 3).getBytes()) {
			String binary = String.format("%8s", Integer.toBinaryString(dt & 0xFF)).replace(' ', '0');
	        System.out.print(binary + " ");
		}
		System.out.println(" ... ");
		return any.toByteArray();
	}
	
	public static byte[] twoInt(int i, int j) {
		TwoInt pb = TwoInt.newBuilder()
				.setI1(i)
				.setI2(j)
				.build();
		return pb.toByteArray();
	}
	
	public static byte[] twoDiff(int i, String s1) {
		TwoDiff pb = TwoDiff.newBuilder()
				.setI1(i)
				.setS1(s1)
				.build();
		return pb.toByteArray();
	}
	
	public static byte[] nestSingle(int i, int j) {
		SingleInt si = SingleInt.newBuilder()
				.setI1(j)
				.build();
		Nest nest = Nest.newBuilder()
				.setI1(i)
				.setS1(si)
				.build();
		return nest.toByteArray();		
	}
	
	public static void display(byte[] data) {
		System.out.print("protobuf binary : ");
		for (byte dt : data) {
			String binary = String.format("%8s", Integer.toBinaryString(dt & 0xFF)).replace(' ', '0');
	        System.out.print(binary + " ");
		}
		System.out.println();
	}
	
	private static String getUrl(com.google.protobuf.Message message) {
		return getTypeUrl("type.googleapis.com",
                message.getDescriptorForType());
	}
	
	private static String getTypeUrl(
		      java.lang.String typeUrlPrefix,
		      com.google.protobuf.Descriptors.Descriptor descriptor) {
		    return typeUrlPrefix.endsWith("/")
		        ? typeUrlPrefix + descriptor.getFullName()
		        : typeUrlPrefix + "/" + descriptor.getFullName();
		  }
	
}
