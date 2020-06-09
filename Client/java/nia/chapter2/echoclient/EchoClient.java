package nia.chapter2.echoclient;
import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.Socket;

public class EchoClient {
	private final String host;
	private final int port;
	
	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public static void main(String[] args) throws InterruptedException {
		String host = "localhost";
		int port = 25;
		new EchoClient(host, port).start();
	}
	
	private void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();	//NIO 처리를 다루는 이벤트 루프 인스턴스 생성
		try {
			Bootstrap b = new Bootstrap();				//클라이언트 를 셋팅 할수 있는 인스턴스 bootStrap 생성
			b.group(group)								
			.channel(NioSocketChannel.class)			// Nio 전송 채널을 사용 하도록 셋팅
			.remoteAddress(new InetSocketAddress(host, port))
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());	//EchoClientHandler 을 pipeline으로 설정한다.
				}
			});
			ChannelFuture f = b.bind().sync();	//서버를 비동기 식으로 바인딩 한다. sync() 는 바인딩이 완료되기를 대기한다.
			/**
			 * ChannelFuture 는 작업이 완료되면 그 결과에 접근 할 수 있게 해주는 자리 표시자 역활을 하는 인터페이스
			 */
			f.channel().closeFuture().sync();	//채널의 CloseFuture를 얻고 완료 될때 까지 현재 스레드를 블로킹한다.
		}finally {
			group.shutdownGracefully().sync();	//EventLoopGroup 을 종료 하고 모든 리소스를 해제 한다.
		}
	}	
}