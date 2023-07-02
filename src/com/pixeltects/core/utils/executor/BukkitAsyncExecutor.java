package com.pixeltects.core.utils.executor;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

public class BukkitAsyncExecutor implements Executor {
    @NonNull
    private final Plugin plugin;

    public static BukkitAsyncExecutor create(@NonNull Plugin plugin) {
        return new BukkitAsyncExecutor(plugin);
    }

    private BukkitAsyncExecutor(@NonNull Plugin plugin) {
        if (plugin == null)
            throw new NullPointerException("plugin is marked @NonNull but is null");
        this.plugin = plugin;
    }

    public void execute(@Nonnull Runnable command) {
        Preconditions.checkNotNull(command, "command");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, command);
    }
}

