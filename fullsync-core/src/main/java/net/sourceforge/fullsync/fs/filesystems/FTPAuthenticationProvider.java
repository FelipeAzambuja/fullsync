/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301, USA.
 *
 * For information about the authors of this project Have a look
 * at the AUTHORS file in the root of this project.
 */
package net.sourceforge.fullsync.fs.filesystems;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import net.sourceforge.fullsync.ConnectionDescription;
import net.sourceforge.fullsync.fs.FileSystemAuthProvider;

class FTPAuthenticationProvider implements FileSystemAuthProvider {
	@Override
	public final void authSetup(final ConnectionDescription description, final FileSystemOptions options) throws FileSystemException {
		String username = description.getParameter(ConnectionDescription.PARAMETER_USERNAME);
		String password = description.getSecretParameter(ConnectionDescription.PARAMETER_PASSWORD);
		StaticUserAuthenticator auth = new StaticUserAuthenticator(null, username, password);
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);
		DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(options, auth);
	}
}
