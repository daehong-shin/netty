package nia.chapter2.echoclient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	/**
	 * 서버로 연결이 만들어지면 channelActive 메소드가 호출된다. 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
	}

	/**
	 * 서버에서 메시지를 수신 하면 channelRead0 메소드가 호출된다.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("client received : " + msg.toString(CharsetUtil.UTF_8));
	}

	/**
	 * 예외 발생시 exceptionCaught 메소드가 호출된다.
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}