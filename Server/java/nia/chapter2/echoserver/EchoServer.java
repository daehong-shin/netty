package nia.chapter2.echoserver;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private final int port;
	
	public static void main(String[] args) throws InterruptedException {
		int port = 25;
		new EchoServer(port).start();
	}

	public EchoServer(int port) {
		this.port = port;
	}
	
	private void start() throws InterruptedException {
		final EchoServerHandler serverHandler = new EchoServerHandler();
		EventLoopGroup group = new NioEventLoopGroup();	//NIO 처리를 다루는 멀티스레드 이벤트 루프 인스턴스 생성
		try {
			ServerBootstrap b = new ServerBootstrap();  //ServerBootstrap 인스턴스 생성. ServerBootstrap은 서버 Channel 을 셋팅 할 수 있는 클래스 
			b.group(group)
				.channel(NioServerSocketChannel.class)					// Nio 전송 채널을 사용 하도록 셋팅
				.localAddress(new InetSocketAddress(port))				// 서버 포트 주소 설정
				.childHandler(new ChannelInitializer<SocketChannel>() { // child 이벤트 핸들러를 설정
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(serverHandler);			//serverHandler 을 pipeline으로 설정 한다.
					}
				});
			
			ChannelFuture f = b.bind().sync();	//서버를 비동기 식으로 바인딩 한다. sync() 는 바인딩이 완료되기를 대기한다.
			/**
			 * ChannelFuture 는 작업이 완료되면 그 결과에 접근 할 수 있게 해주는 자리 표시자 역활을 하는 인터페이스
			 */
			f.channel().closeFuture().sync();	//채널의 CloseFuture를 얻고 완료 될때 까지 현재 스레드를 블로킹한다.
		} finally {
			group.shutdownGracefully().sync();	//EventLoopGroup 을 종료 하고 모든 리소스를 해제 한다.
		}
	}
}