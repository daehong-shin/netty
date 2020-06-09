package nia.chapter2.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
	/**
	 * 메시지가 들어올때 호출되는 메소드
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		System.out.println(
				"Server received : " + in.toString(CharsetUtil.UTF_8)
				);
		ctx.write(in);
	}

	/**
	 * channelRead 메소드가 처리 완료 되었다는 것을 핸들러에게 통보 하는 메소드
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
			.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 읽기 작업중 오류가 발생 했을 경우 호출 되는 메소드
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}