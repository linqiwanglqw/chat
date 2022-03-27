package com.lin.netty.chat;

import com.sun.scenario.effect.impl.sw.java.JSWBrightpassPeer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//定义配置类
@Configuration
public class ChatServer {

    @Autowired
    ChatHandler chatHandler;

    /**
     * bossGroup只是处理连接请求
     * @return
     */
    @Bean
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }

    /**
     * workGroup用于与客户端业务处理
     * @return
     */
    @Bean
    public NioEventLoopGroup workGroup(){
        return new NioEventLoopGroup();
    }

    @Bean("bootstrap")
    public ServerBootstrap bootstrap(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置两个线程组
        serverBootstrap.group(bossGroup(),workGroup())
                //使用NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                //给我们的workGroup的EventLoop对应的管道设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //解码成Http
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        //向客户端发送html文件
                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                        //多条信息整合为一条
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
                        socketChannel.pipeline().addLast(chatHandler);

                    }
                })
                //线程池的最大线程数
                .option(ChannelOption.SO_BACKLOG,100)
                //线程池的核心参数
                .childOption(ChannelOption.SO_KEEPALIVE,true);
        return serverBootstrap;
    }












}
